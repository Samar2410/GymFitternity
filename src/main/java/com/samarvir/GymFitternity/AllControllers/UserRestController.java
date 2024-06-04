/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.samarvir.GymFitternity.AllControllers;
import java.sql.*;
import com.samarvir.GymFitternity.vmm.DBLoader;
import com.samarvir.GymFitternity.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author samar
 */
@RestController
public class UserRestController {
    private int otp;
    @Autowired
    private EmailSenderService service;
    
    @GetMapping("/UserGetCities")
    public String userGetCities(){
        return new RDBMS_TO_JSON().generateJSON("select * from cities");
    }
    
    @GetMapping("/UserGetGyms")
    public String userGetGyms(@RequestParam String cityname){
        System.out.println(cityname);
        return new RDBMS_TO_JSON().generateJSON("select g.id, g.name,g.address,g.photo from  gyms g  inner join owners o on o.email=g.email where g.status='Approved' and o.Status='Approved' and g.city='"+cityname+"'");
    }
    
    @GetMapping("/UserGetGymData")
    public String userGetGymData(@RequestParam int gymid){
         return new RDBMS_TO_JSON().generateJSON("select * from gyms g left join photos p on g.id = p.gymid where g.id="+gymid);
        
    }
    @PostMapping("/AddUser")
    public String addUser(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam String add, @RequestParam String mobile){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from users where email='"+email+"'");
            if(rs.next()){
                ans="fail";
            }
            else {
                rs.moveToInsertRow();
                rs.updateString("name", name);
                 rs.updateString("email", email);
                  rs.updateString("password", password);
                   rs.updateString("address", add);
                    rs.updateString("mobile", mobile);
                    rs.insertRow();
                    ans="success";
            }
            
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/CheckUserLogin")
    public String checkUserLogin(@RequestParam String email,@RequestParam String password,HttpSession session){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from users where email='"+email+"' and password='"+password+"'");
            if(rs.next()){
                ans="success";
               session.setAttribute("useremail", email);
            }
            else ans="fail";
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
        
    }
    
    @GetMapping("/UserGetPackageData")
    public String userGetPackageData(@RequestParam int packageid){
        return new RDBMS_TO_JSON().generateJSON("select id,price,duration from packages where id="+packageid);
    }
    
    @GetMapping("/UserGetGymAndPackageData")
    public String userGetGymAndPackageData(@RequestParam int packageid){
        return new RDBMS_TO_JSON().generateJSON("select g.id as 'gymid',g.email,g.address,p.id as 'packageid',p.name as 'packagename',p.price,p.duration from gyms g inner join packages p on p.gymid=g.id where p.id="+packageid);
    }
    @GetMapping("/CashPayment")
    public String cashPayment(HttpSession session,@RequestParam int packageid,@RequestParam String start,@RequestParam String end,@RequestParam String packagename,@RequestParam String price,@RequestParam int commission,@RequestParam String duration,@RequestParam int gymid,@RequestParam String gymemail,@RequestParam String address){
        String ans="";
        
        String useremail=(String)session.getAttribute("useremail");
        try {
            
            ResultSet rs=DBLoader.executeQuery("select * from payment");
           
                rs.moveToInsertRow();
                rs.updateString("user_email",useremail);
                rs.updateInt("package_id", packageid);
                rs.updateString("package_name",packagename);
                
                rs.updateString("price",price);
                rs.updateInt("commission", commission);
                rs.updateString("payment_type","Cash");
                rs.updateString("start_date",start);
                rs.updateString("end_date",end);
                rs.updateString("address",address);
                rs.updateString("owner_email",gymemail);
                rs.insertRow();
                ans="success";
               ResultSet rs2=DBLoader.executeQuery("select name from gyms where email='"+gymemail+"'");
               String gymname="";
            if(rs2.next()){
                gymname=rs2.getString("name");
            }
            ResultSet rs3=DBLoader.executeQuery("select name from users where email='"+useremail+"'");
               String username="";
            if(rs3.next()){
                username=rs3.getString("name");
            }
                String body="Dear "+username+",\n" +
"\n" +
"Welcome to the "+gymname+" family! We are thrilled to have you on board and excited to embark on this fitness journey with you. Your recent purchase of our gym package marks the beginning of a healthier, stronger you, and we’re here to support you every step of the way.\n" +
"\n" +
"Here are a few key details and next steps to ensure you get the most out of your membership:\n" +
"\n" +
"1. Membership Activation: You have selected the "+packagename+" package of price Rs. "+price+" Your membership is now active! You can start using our facilities from "+start+".\n" +
"\n" +
"2. Class Schedules: Your training starts from "+start+" till "+end+" . From yoga to strength training, we offer a range of classes to suit every preference.\n" +
"\n" +
"3. Personal Training: Interested in personalized fitness guidance? Our certified trainers are here to help you create a customized workout plan tailored to your needs.\n" +
"\n" +
"4. Facility Amenities: Explore our state-of-the-art equipment, locker rooms, and additional amenities designed to enhance your gym experience.\n" +
"\n" +

"6. Stay Connected: Follow us on social media to stay updated on gym news, fitness tips, and community events. Feel free to reach out to our staff with any questions or feedback.\n" +
"\n" +
"We are committed to providing you with a positive and rewarding fitness journey at "+gymname+". If there’s anything we can do to enhance your experience, please let us know.\n" +
"\n" +
"Thank you for choosing "+gymname+"! Let’s crush those fitness goals together 💪\n" +
"\n" +
"Best regards,\n" +
"\n" +
"Samarvir Singh\n" +
"Admin\n";
                String ownername="";
                ResultSet rs4=DBLoader.executeQuery("select Name from owners where Email='"+gymemail+"'");
                if(rs4.next()){
                    ownername=rs4.getString("Name");
                }
                String body2="Dear "+ownername+",\n" +
"\n" +
"I hope this message finds you well.\n" +
"\n" +
"I am writing to inform you that a new membership package has been purchased at "+gymname+". Below are the details of the transaction:\n" +
"\n" +
"Member Name: "+username+"\n" +
"Membership Package: "+packagename+"\n" +
"Start Date: "+start+"\n" +
"End Date: "+end+"\n" +
"Payment Method: Cash\n" +

"Please ensure that the new member receives all necessary information regarding their membership, including how to obtain their membership card, an introduction to the gym facilities, and a schedule of any included classes or programs.\n" +
"\n" +
"If you require any additional information or have any questions regarding this purchase, please do not hesitate to contact me.\n" +

"\n" +
"Best regards,\n" +
"\n" +
"Samarvir Singh\n" +
"Admin\n" +
"Gym Fitternity";
            service.sendSimpleEmail("samarvirnarula@gmail.com", body, "Welcome to "+gymname+"! Your Fitness Journey Starts Now 🏋️‍♂️");
            service.sendSimpleEmail("samarvirnarula@gmail.com", body2, "New Membership Package Purchase Notification️");
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    @GetMapping("/OnlinePayment")
    public String onlinePayment(HttpSession session,@RequestParam int packageid,@RequestParam String start,@RequestParam String end,@RequestParam String packagename,@RequestParam String price,@RequestParam int commission,@RequestParam String duration,@RequestParam int gymid,@RequestParam String gymemail,@RequestParam String address){
        String ans="";
        
        String useremail=(String)session.getAttribute("useremail");
        try {
            ResultSet rs=DBLoader.executeQuery("select * from payment");
           
       
                rs.moveToInsertRow();
                rs.updateString("user_email",useremail);
                rs.updateInt("package_id", packageid);
                rs.updateString("package_name",packagename);
                rs.updateString("price",price);
                rs.updateInt("commission", commission);
                rs.updateString("payment_type","Online");
                rs.updateString("start_date",start);
                rs.updateString("end_date",end);
                rs.updateString("address",address);
                rs.updateString("owner_email",gymemail);
                rs.insertRow();
                ans="success";
                ResultSet rs2=DBLoader.executeQuery("select name from gyms where email='"+gymemail+"'");
               String gymname="";
            if(rs2.next()){
                gymname=rs2.getString("name");
            }
            ResultSet rs3=DBLoader.executeQuery("select name from users where email='"+useremail+"'");
               String username="";
            if(rs3.next()){
                username=rs3.getString("name");
            }
                String body="Dear "+username+",\n" +
"\n" +
"Welcome to the "+gymname+" family! We are thrilled to have you on board and excited to embark on this fitness journey with you. Your recent purchase of our gym package marks the beginning of a healthier, stronger you, and we’re here to support you every step of the way.\n" +
"\n" +
"Here are a few key details and next steps to ensure you get the most out of your membership:\n" +
"\n" +
"1. Membership Activation: Your payment was successful. You have selected the "+packagename+" package of price Rs. "+price+" Your membership is now active! You can start using our facilities from "+start+".\n" +
"\n" +
"2. Class Schedules: Your training starts from "+start+" till "+end+" . From yoga to strength training, we offer a range of classes to suit every preference.\n" +
"\n" +
"3. Personal Training: Interested in personalized fitness guidance? Our certified trainers are here to help you create a customized workout plan tailored to your needs.\n" +
"\n" +
"4. Facility Amenities: Explore our state-of-the-art equipment, locker rooms, and additional amenities designed to enhance your gym experience.\n" +
"\n" +

"6. Stay Connected: Follow us on social media to stay updated on gym news, fitness tips, and community events. Feel free to reach out to our staff with any questions or feedback.\n" +
"\n" +
"We are committed to providing you with a positive and rewarding fitness journey at "+gymname+". If there’s anything we can do to enhance your experience, please let us know.\n" +
"\n" +
"Thank you for choosing "+gymname+"! Let’s crush those fitness goals together 💪\n" +
"\n" +
"Best regards,\n" +
"\n" +
"Samarvir Singh\n" +
"Admin\n";
                String ownername="";
                ResultSet rs4=DBLoader.executeQuery("select Name from owners where Email='"+gymemail+"'");
                if(rs4.next()){
                    ownername=rs4.getString("Name");
                }
                String body2="Dear "+ownername+",\n" +
"\n" +
"I hope this message finds you well.\n" +
"\n" +
"I am writing to inform you that a new membership package has been purchased at "+gymname+". Below are the details of the transaction:\n" +
"\n" +
"Member Name: "+username+"\n" +
"Membership Package: "+packagename+"\n" +
"Start Date: "+start+"\n" +
"End Date: "+end+"\n" +
"Payment Method: Online\n" +

"Please ensure that the new member receives all necessary information regarding their membership, including how to obtain their membership card, an introduction to the gym facilities, and a schedule of any included classes or programs.\n" +
"\n" +
"If you require any additional information or have any questions regarding this purchase, please do not hesitate to contact me.\n" +

"\n" +
"Best regards,\n" +
"\n" +
"Samarvir Singh\n" +
"Admin\n" +
"Gym Fitternity";
                        service.sendSimpleEmail("samarvirnarula@gmail.com", body,"Welcome to "+gymname+"! Your Fitness Journey Starts Now 🏋️‍♂️");
                        service.sendSimpleEmail("samarvirnarula@gmail.com", body2, "New Membership Package Purchase Notification️");
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/UserFetchBookings")
    public String userFetchBookings(HttpSession session){
        String useremail=(String)session.getAttribute("useremail");
        return new RDBMS_TO_JSON().generateJSON("select g.name as 'gymname',g.city,p.owner_email,p.package_name,p.price,p.start_date,p.end_date   from payment p inner join gyms g on g.address=p.address where user_email='"+useremail+"'");
        
    }
    
    @GetMapping("/UserChangePass")
    public String userChangePass(@RequestParam String old,@RequestParam String newp,HttpSession session){
        String ans="";
        String useremail=(String)session.getAttribute("useremail");
        try {
            ResultSet rs=DBLoader.executeQuery("select * from users where email='"+useremail+"' and password='"+old+"'");
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
    
    @GetMapping("/UserSendOtp")
    public String userGetOtp(@RequestParam String email){
        String ans="";
        try {
            ResultSet rs=DBLoader.executeQuery("select * from users where email='"+email+"'");
            if(rs.next()){
                int l=100000;
                int u=999999;
                otp=l+(int)((u-l)*Math.random());
                
               String username=rs.getString("name");
                            String body="Dear "+username+",\n" +
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
    
    @GetMapping("/UserCheckOtp")
    public String userCheckOtp(@RequestParam String userotp){
        
        
        if(userotp.equals(otp+""))return "success";
        
        else return "fail";
    }
    @GetMapping("/UserUpdatePassword")
    public String userUpdatePassword(@RequestParam String password,@RequestParam String email){
        String ans="";
        
        try {
            ResultSet rs=DBLoader.executeQuery("select * from users where email='"+email+"'");
            if(rs.next()){
                rs.updateString("password", password);
                rs.updateRow();
                ans="success";
            }
           
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/UserAddReview")
    public String userAddReview(HttpSession session,@RequestParam String comment,@RequestParam int stars,@RequestParam String gymemail){
        String ans="";
        String useremail=(String)session.getAttribute("useremail");
        try {
            ResultSet rs=DBLoader.executeQuery("select * from review");
            
            
                rs.moveToInsertRow();
                rs.updateInt("rating",stars);
                rs.updateString("comment", comment);
                 rs.updateString("owneremail", gymemail);
                  rs.updateString("useremail", useremail);
                  
                  rs.insertRow();
                  ans="success";
            
        } catch (Exception e) {
            ans=e.toString();
        }
        return ans;
    }
    
    @GetMapping("/UserShowRatings")
    public String userShowRatings(@RequestParam String gymemail){
        return new RDBMS_TO_JSON().generateJSON("select * from review where owneremail='"+gymemail+"'");
        
    }
    @GetMapping("/GetCommissionDetails")
    public String getCommissionDetails(){
        return new RDBMS_TO_JSON().generateJSON("select * from commissions");
    }
}
