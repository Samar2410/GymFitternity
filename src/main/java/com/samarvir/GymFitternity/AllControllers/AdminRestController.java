
package com.samarvir.GymFitternity.AllControllers;
import java.sql.*;
import com.samarvir.GymFitternity.vmm.DBLoader;
import com.samarvir.GymFitternity.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AdminRestController {
    @Autowired
    private EmailSenderService service;
    
    @GetMapping("/CheckAdminLogin")
    public String checkAdminLogin(@RequestParam String email,@RequestParam String password,HttpSession session){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from adminlogin where email='"+email+"' and password ='"+password+"'");
            if(rs.next()){
                ans=ans+"success";
                session.setAttribute("adminemail", email);
            }
            else
            {
            ans=ans+"fail";
            }
        
        } catch (Exception e) {
           return e.toString();
        }
        return ans;
    }
    
    @PostMapping("/AdminAddCity")
    public String adminAddCity(@RequestParam String city,@RequestParam String desc,@RequestParam MultipartFile photo){
        String ans="";
        
        String origname=photo.getOriginalFilename();
        
        
        String projectpath =System.getProperty("user.dir");
        String internalpath="/src/main/resources/static/";
        String folder="myuploads/";
        try {
            FileOutputStream fos=new FileOutputStream(projectpath+internalpath+folder+origname);
            byte[] b=photo.getBytes();
            fos.write(b);
            fos.close();
            
           ResultSet rs=DBLoader.executeQuery("select * from cities where cityname='"+city+"'");
           if(rs.next()){
               ans=ans+"fail";
           }
           else{
                rs.moveToInsertRow();
                rs.updateString("cityname", city);
                rs.updateString("desc", desc);
                rs.updateString("photo", folder+origname);
                rs.insertRow();
                ans=ans+"success";
           }

        } catch (Exception e) {
           ans=ans+e.toString();
        }
               return ans;
    }
    @GetMapping("/ShowCities")
    public String showCities(){
        return new RDBMS_TO_JSON().generateJSON("select * from cities");
    }
    @GetMapping("/RemoveCity")
    public String removeCity(@RequestParam String city){
       String ans="";
        try {
            
            ResultSet rs=DBLoader.executeQuery("select * from cities where cityname='"+city+"'");
            if(rs.next()){
                rs.deleteRow();
                ans="success";
            }
            else ans="fail";
        } catch (Exception e) {
            ans+=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/FetchFranchises")
    public String fetchFranchises(){
        return new RDBMS_TO_JSON().generateJSON("select * from owners");
    }
    @GetMapping("/ApproveFranchise")
    public String approveFranchise(@RequestParam String email){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from owners where Email='"+email+"'");
            if(rs.next()){
                rs.updateString("Status", "Approved");
                rs.updateRow();
                ans="success";
                ResultSet rs2=DBLoader.executeQuery("select Name,FranchiseName from owners where email='"+email+"'");
               String ownername="",fname="";
            if(rs2.next()){
                ownername=rs2.getString("Name");
                fname=rs2.getString("FranchiseName");
            }
                String body="Dear "+ownername+",\n" +
"\n" +
"I am pleased to inform you that your application for the "+fname+" franchise has been approved. Congratulations on this achievement!\n" +
"\n" +
"We look forward to a successful partnership and are excited about the opportunities ahead.\n" +
"\n" +
"Welcome to GymFitternity family!\n" +
"\n" +
"Best regards,\n" +
"\n" +
"Samarvir Singh\n" +
"Admin\n"
;
                service.sendSimpleEmail("samarvirnarula@gmail.com", body, "Franchise Approval Notification️");
            }
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
     @GetMapping("/BlockFranchise")
    public String blockFranchise(@RequestParam String email){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from owners where Email='"+email+"'");
            if(rs.next()){
                rs.updateString("Status", "Pending");
                rs.updateRow();
                ans="success";
                
                
                ResultSet rs2=DBLoader.executeQuery("select Name,FranchiseName from owners where email='"+email+"'");
               String ownername="",fname="";
            if(rs2.next()){
                ownername=rs2.getString("Name");
                fname=rs2.getString("FranchiseName");
            }
            String body="Dear "+ownername+",\n" +
"\n" +
"We regret to inform you that your franchise "+fname+" has been blocked.\n" +
"\n" +
"If you have any questions or believe this action was taken in error, please contact us for further assistance.\n" +
"\n" +
"Thank you for your understanding.\n" +
"\n" +
"Best regards,\n" +
"\n" +
"Samarvir Singh\n" +
"Admin";
            service.sendSimpleEmail("samarvirnarula@gmail.com", body, "Your Franchise Listing Has Been Blocked");
            }
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/FetchGyms")
    public String fetchGyms(){
        return new RDBMS_TO_JSON().generateJSON("select * from gyms");
    }
    
    @GetMapping("/ChangeGymStatus")
    public String changeGymStatus(@RequestParam int id,@RequestParam String status){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from gyms where id="+id);
            if(rs.next()){
                String gymemail=rs.getString("email");
                String gymname=rs.getString("name");
                ResultSet rs2=DBLoader.executeQuery("select Name from owners where Email='"+gymemail+"'");
               String ownername="";
            if(rs2.next()){
                ownername=rs2.getString("Name");
                
            }
                if(status.equals("Pending")){
                    rs.updateString("status", "Approved");
                    rs.updateRow();
                    ans="Approved";
                    String body="Dear "+ownername+",\n" +
"\n" +
"I am pleased to inform you that your application for the gym "+gymname+"  has been approved. Congratulations on this achievement!\n" +
"\n" +
"We look forward to a successful partnership and are excited about the opportunities ahead.\n" +
"\n" +
"Best regards,\n" +
"\n" +
"Samarvir Singh\n" +
"Admin\n"
;
                service.sendSimpleEmail("samarvirnarula@gmail.com", body, "Gym Approval Notification️");
                    
                }
                else {
                    rs.updateString("status", "Pending");
                    rs.updateRow();
                    ans="Pending";
                    
            String body="Dear "+ownername+",\n" +
"\n" +
"We regret to inform you that your gym "+gymname+" has been blocked.\n" +
"\n" +
"If you have any questions or believe this action was taken in error, please contact us for further assistance.\n" +
"\n" +
"Thank you for your understanding.\n" +
"\n" +
"Best regards,\n" +
"\n" +
"Samarvir Singh\n" +
"Admin";
            service.sendSimpleEmail("samarvirnarula@gmail.com", body, "Your Gym Has Been Blocked");
                }
                
            }
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    @GetMapping("/AdminChangePass")
    public String adminChangePass(@RequestParam String old,@RequestParam String newp,HttpSession session){
        String ans="";
        String adminemail=(String)session.getAttribute("adminemail");
        try {
            ResultSet rs=DBLoader.executeQuery("select * from adminlogin where email='"+adminemail+"' and password='"+old+"'");
            if(rs.next()){
                rs.updateString("password", newp);
                rs.updateRow();
                ans="success";
            }
            else ans="fail";
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    @GetMapping("AdminGetCommissions")
    public String adminGetCommissions(){
        return new RDBMS_TO_JSON().generateJSON("select * from commissions");
    }
    @GetMapping("/UpdateCommission")
    public String updateCommmission(@RequestParam int commission,@RequestParam String type){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from commissions where commission_type='"+type+"'");
            if(rs.next()){
                System.out.println(rs.getInt("commission"));
                rs.updateInt("commission", commission);
                rs.updateRow();
                ans="success";
                
            }
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    @GetMapping("/SetCommissionType")
    public String setCommissionType(@RequestParam String type){
        
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from commissions");
            while(rs.next()){
                String ctype=rs.getString("commission_type");
                System.out.println(ctype);
                System.out.println(type);
                if(ctype.equals(type))rs.updateString("status", "Active");
                else rs.updateString("status", "Inactive");
                rs.updateRow();
            }
            ans="success";
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    @GetMapping("/CalcEarnings")
    public String calcEarnings(){
        String ans="";
        int amount=0;
        try {
            ResultSet rs=DBLoader.executeQuery("select commission from payment");
            while(rs.next()){
                amount+=rs.getInt("commission");
            }
            ans=amount+"";
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    
}
