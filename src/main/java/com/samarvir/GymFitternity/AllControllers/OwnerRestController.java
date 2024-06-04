/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.samarvir.GymFitternity.AllControllers;

import java.sql.*;
import com.samarvir.GymFitternity.vmm.DBLoader;
import com.samarvir.GymFitternity.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author samar
 */
@RestController
public class OwnerRestController {
private int otp;
    @Autowired
    private EmailSenderService service;
    @PostMapping("/AddOwner")
    public String addOwner(@RequestParam String name, @RequestParam String email, @RequestParam String franchise, @RequestParam MultipartFile photo, @RequestParam String password, @RequestParam String desc, @RequestParam String phone) {
        String ans = "";
        String origname = photo.getOriginalFilename();

        String projectpath = System.getProperty("user.dir");
        String internalpath = "/src/main/resources/static/";
        String folder = "myuploads/";
        try {

            ResultSet rs = DBLoader.executeQuery("select * from owners where email='" + email+"'");
            if (rs.next()) {
                ans = "fail";
            } else {
                FileOutputStream fos = new FileOutputStream(projectpath + internalpath + folder + origname);
                byte[] b = photo.getBytes();
                fos.write(b);
                fos.close();
                rs.moveToInsertRow();
                rs.updateString("Name", name);
                rs.updateString("Email", email);
                rs.updateString("FranchiseName", franchise);
                rs.updateString("Photo", folder + origname);
                rs.updateString("Password", password);
                rs.updateString("Description", desc);
                rs.updateString("PhoneNumber", phone);
                rs.updateString("Status", "Pending");
                rs.insertRow();
                ans+="success";

            }
        } catch (Exception e) {
            ans+=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/GetCities")
    public String getCities(){
       return new RDBMS_TO_JSON().generateJSON("select * from cities");
    }
    
    @PostMapping("/OwnerAddGym")
    public String ownerAddGym(HttpSession session,@RequestParam String gym, @RequestParam String city, @RequestParam String mobile, @RequestParam String longitude, @RequestParam String latitude, @RequestParam String address, @RequestParam MultipartFile photo,@RequestParam String pool,@RequestParam String AC,@RequestParam String change,@RequestParam String shower,@RequestParam String park,@RequestParam String water) {
        String ans = "";
        String origname = photo.getOriginalFilename();

        String projectpath = System.getProperty("user.dir");
        String internalpath = "/src/main/resources/static/";
        String folder = "myuploads/";
        String email=(String)session.getAttribute("owneremail");
        try {

            ResultSet rs = DBLoader.executeQuery("select * from gyms where name='" + gym+"'");
            if (rs.next()) {
                ans = "fail";
            } else {
                FileOutputStream fos = new FileOutputStream(projectpath + internalpath + folder + origname);
                byte[] b = photo.getBytes();
                fos.write(b);
                fos.close();
                rs.moveToInsertRow();
                rs.updateString("name", gym);
                rs.updateString("city", city);
                rs.updateString("mobile", mobile);
                rs.updateString("latitude", latitude);
                rs.updateString("longitude", longitude);
                rs.updateString("photo", folder + origname);
                rs.updateString("address", address);
                
                rs.updateString("email",email );
                rs.updateString("status", "Pending");
                rs.updateString("Pool", pool);
                rs.updateString("AC", AC);
                rs.updateString("ChangingRoom", change);
                rs.updateString("Shower", shower);
                rs.updateString("ParkingFacility", park);
                rs.updateString("WaterFacility", water);
                rs.insertRow();
                ans+="success";

            }
        } catch (Exception e) {
            ans+=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/OwnerShowGyms")
    public String ownerShowGyms(HttpSession session){
        String email=(String)session.getAttribute("owneremail");
        return new RDBMS_TO_JSON().generateJSON("select * from gyms where email='"+email+"'");
    }
    
    @GetMapping("/DeleteGym")
    public String deleteGym(@RequestParam String id){
        String s="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from gyms where id='"+id+"'");
            if(rs.next()){
                rs.deleteRow();
                s="success";
            }
        } catch (Exception e) {
            s=e.toString();
        }
        return s;
    }
    @GetMapping("/CheckOwnerLogin")
    public String checkOwnerLogin(@RequestParam String email,@RequestParam String password,HttpSession session){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from owners where Email='"+email+"' and Password ='"+password+"'");
            if(rs.next()){
                if(rs.getString("Status").equals("Approved")){
                    ans=ans+"success";
                    session.setAttribute("owneremail", email);
                }
                else ans="Pending";
                
                
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
    
    @PostMapping("/OwnerAddGymPhoto")
    public String ownerAddGymPhoto(@RequestParam int id,@RequestParam MultipartFile photo){
        String ans="";
        String origname = photo.getOriginalFilename();
      
        String projectpath = System.getProperty("user.dir");
        String internalpath = "/src/main/resources/static/";
        String folder = "myuploads/";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from photos");
            rs.moveToInsertRow();
            rs.updateInt("gymid", id);
            rs.updateString("path", folder+origname);
            rs.insertRow();
            FileOutputStream fos = new FileOutputStream(projectpath + internalpath + folder + origname);
                byte[] b = photo.getBytes();
                fos.write(b);
                fos.close();
            ans="success";
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/GetGyms")
    public String getGyms(HttpSession session){
        String owneremail=(String)session.getAttribute("owneremail");
        return new RDBMS_TO_JSON().generateJSON("select * from gyms where email='"+owneremail+"'");
    }
    
    @GetMapping("/GetGymData")
    public String getGymData(@RequestParam String id){
        return new RDBMS_TO_JSON().generateJSON("select * from gyms where id='"+id+"'");
    }
    
    @PostMapping("/OwnerApplyChangesToGym")
    public String ownerApplyChangesToGym(HttpSession session,@RequestParam String id,@RequestParam String gym, @RequestParam String city, @RequestParam String mobile, @RequestParam String longitude, @RequestParam String latitude, @RequestParam String address, @RequestParam MultipartFile photo,@RequestParam String pool,@RequestParam String AC,@RequestParam String change,@RequestParam String shower,@RequestParam String park,@RequestParam String water) {
        String ans = "";
        String origname = photo.getOriginalFilename();

        String projectpath = System.getProperty("user.dir");
        String internalpath = "/src/main/resources/static/";
        String folder = "myuploads/";
        String email=(String)session.getAttribute("owneremail");
        try {

            ResultSet rs = DBLoader.executeQuery("select * from gyms where id='"+id+"'");
            if (rs.next()) {
               
            
                FileOutputStream fos = new FileOutputStream(projectpath + internalpath + folder + origname);
                byte[] b = photo.getBytes();
                fos.write(b);
                fos.close();
                //rs.moveToCurrentRow();
                rs.updateString("name", gym);
                rs.updateString("city", city);
                rs.updateString("mobile", mobile);
                rs.updateString("latitude", latitude);
                rs.updateString("longitude", longitude);
                rs.updateString("photo", folder + origname);
                rs.updateString("address", address);
                
                rs.updateString("email",email );
                rs.updateString("status", "Pending");
                rs.updateString("Pool", pool);
                rs.updateString("AC", AC);
                rs.updateString("ChangingRoom", change);
                rs.updateString("Shower", shower);
                rs.updateString("ParkingFacility", park);
                rs.updateString("WaterFacility", water);
                rs.updateRow();
                ans+="success";

            }
        } catch (Exception e) {
            ans+=e.toString();
        }
        return ans;
    }
    @GetMapping("/AddPackage")
    public String addPackage(@RequestParam int gymid,@RequestParam String name,@RequestParam String price,@RequestParam String dur,@RequestParam String desc){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from packages where name='"+name+"'");
            if(rs.next()){
                ans="fail";
            }
            else{
                rs.moveToInsertRow();
                rs.updateString("name", name);
                rs.updateString("price", price);
                rs.updateString("duration", dur);
                rs.updateString("description", desc);
                rs.updateInt("gymid", gymid);
                rs.insertRow();
                ans="success";
            }
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/ShowPackages")
    public String showPackages(@RequestParam int gymid){
        return new RDBMS_TO_JSON().generateJSON("select * from packages where gymid="+gymid);
    }
    
    @GetMapping("/DeletePackage")
    public String deletePackage(@RequestParam int id){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from packages where id="+id);
            if(rs.next()){
                rs.deleteRow();
                ans="success";
            }
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/DeletePhoto")
    public String deletePhoto(@RequestParam int id){
        String ans="";
        
        try {
            ResultSet rs=DBLoader.executeQuery("select * from photos where id="+id);
            if(rs.next()){
                
                rs.deleteRow();
                ans="success";
            }
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    @GetMapping("/OwnerFetchBookings")
    public String ownerFetchBookings(HttpSession session){
        String owneremail=(String)session.getAttribute("owneremail");
        return new RDBMS_TO_JSON().generateJSON("select g.name as 'gymname',p.user_email,p.package_name,p.price,p.start_date,p.end_date   from payment p inner join gyms g on g.address=p.address where owner_email='"+owneremail+"'");
        
    }
    @GetMapping("/OwnerChangePass")
    public String ownerChangePass(@RequestParam String old,@RequestParam String newp,HttpSession session){
        String ans="";
        String owneremail=(String)session.getAttribute("owneremail");
        try {
            ResultSet rs=DBLoader.executeQuery("select * from owners where Email='"+owneremail+"' and Password='"+old+"'");
            if(rs.next()){
                rs.updateString("Password", newp);
                rs.updateRow();
                ans="success";
            }
            else ans="fail";
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    @GetMapping("/OwnerSendOtp")
    public String ownerGetOtp(@RequestParam String email){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from owners where Email='"+email+"'");
            if(rs.next()){
                int l=100000;
                int u=999999;
                otp=l+(int)((u-l)*Math.random());
                
               String ownername="";
            
                ownername=rs.getString("Name");
            
                String body="Dear "+ownername+",\n" +
"\n" +
"We received a request to reset the password for your account associated with this email address. Please use the following One-Time Password (OTP) to complete the process:\n" +
"\n" +
"Your OTP: "+otp+"\n" +
"\n" +
"If you did not request a password reset, please ignore this email or contact our support team immediately.\n" +
"Steps to reset your password:\n" +
"\n" +
"1. Go to the password reset page:\n" +
"2. Enter the OTP provided above.\n" +
"3. Enter new password.\n" +
"For your security, do not share this OTP with anyone. Our support team will never ask for your OTP.\n" +
"\n" +
"If you have any questions or need further assistance, please do not hesitate to contact us at gymfitternity24@gmail.com.\n" +
"\n" +
"Thank you,\n" +
"Samarvir Singh\n" +
"Admin";
                service.sendSimpleEmail("samarvirnarula@gmail.com", body,"Your One-Time Password (OTP) for Password Reset");
                ans="success";
            }
            else ans="fail";
            
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
        
    }
    
    @GetMapping("/OwnerCheckOtp")
    public String ownerCheckOtp(@RequestParam String ownerotp){
        
        
        if(ownerotp.equals(otp+""))return "success";
        
        else return "fail";
    }
    
    @GetMapping("/OwnerUpdatePassword")
    public String ownerUpdatePassword(@RequestParam String password,@RequestParam String email){
        String ans="";
        
        try {
            ResultSet rs=DBLoader.executeQuery("select * from owners where Email='"+email+"'");
            if(rs.next()){
                rs.updateString("Password", password);
                rs.updateRow();
                ans="success";
                System.out.println(ans);
            }
            
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    
    
    
}
