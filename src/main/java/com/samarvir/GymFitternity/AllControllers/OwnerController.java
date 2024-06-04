/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.samarvir.GymFitternity.AllControllers;
import com.samarvir.GymFitternity.model.Photo;
import java.sql.*;
import com.samarvir.GymFitternity.vmm.DBLoader;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author samar
 */
@Controller
public class OwnerController {
    
    @GetMapping("/OwnerSignup")
    public String ownerSignup(){
        return "OwnerSignup";
    }
    @GetMapping("/OwnerHome")
    public String ownerHome(HttpSession session){
            String owneremail=(String)session.getAttribute("owneremail");
           if(owneremail==null)return "redirect:/OwnerLogin";
           else return "ownerhome";
    }
    @GetMapping("/OwnerManageGyms")
    public String ownerManagegyms(HttpSession session){
        String owneremail=(String)session.getAttribute("owneremail");
           if(owneremail==null)return "redirect:/OwnerLogin";
        return "ownermanagegyms";
    }
    @GetMapping("/OwnerLogin")
    public String ownerLogin(){
        return "OwnerLogin";
    }
    
    @GetMapping("/OwnerShowGymPhotos")
    public String ownerAddGymPhotos(@RequestParam int id,Model model,HttpSession session){
        String owneremail=(String)session.getAttribute("owneremail");
           if(owneremail==null)return "redirect:/OwnerLogin";
        
        try {
            ArrayList<Photo>al=new ArrayList<>();
            ResultSet rs=DBLoader.executeQuery("select * from photos where gymid="+id);
            while(rs.next()){
                int photoid=rs.getInt("id");
                String path=rs.getString("path");
                int gymid=rs.getInt("gymid");
                al.add(new Photo(photoid,path,gymid));
            }
                model.addAttribute("photos",al); 
                model.addAttribute("gymid",id);
                return "Photos";
            }
       
           catch (Exception e) {
            return e.toString();
        }
        
        
    }
    @GetMapping("/AllGyms")
    public String allGyms(HttpSession session){
        String owneremail=(String)session.getAttribute("owneremail");
           if(owneremail==null)return "redirect:/OwnerLogin";
        return "allgyms";
    }
    @GetMapping("/OwnerEditGym")
    public String ownerEditGym(@RequestParam String id,Model model,HttpSession session){
        String owneremail=(String)session.getAttribute("owneremail");
           if(owneremail==null)return "redirect:/OwnerLogin";
        model.addAttribute("gymid",id);
        return "ownereditgym";
    }
    
    @GetMapping("/AddPackages")
    public String addPackages(@RequestParam int id,Model model,HttpSession session){
        String owneremail=(String)session.getAttribute("owneremail");
           if(owneremail==null)return "redirect:/OwnerLogin";
        model.addAttribute("gymid",id);
        return "allpackages";
    }
    @GetMapping("/OwnerLogOut")
    public String ownerLogOut(HttpSession session){
        session.removeAttribute("owneremail");
        return "redirect:/";
    }
    
    @GetMapping("/OwnerViewBookings")
    public String ownerViewBookings(HttpSession session){
        String owneremail=(String)session.getAttribute("owneremail");
           if(owneremail==null)return "redirect:/OwnerLogin";
        return "ownerviewbookings";
    }
    @GetMapping("/OwnerChangePassword")
    public String ownerChangePassword(HttpSession session){
        String owneremail=(String)session.getAttribute("owneremail");
           if(owneremail==null)return "redirect:/OwnerLogin";
        return "ownerchangepassword";
    }
     @GetMapping("/OwnerForgotPassword")
    public String ownerForgotPassword(HttpSession session){
       
        return "OwnerForgotPassword";
    }
}
