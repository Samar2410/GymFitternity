
package com.samarvir.GymFitternity.AllControllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
    @GetMapping("/AdminLogin")
    public String adminLogin(){
        return "AdminLogin";
    }
    
    @GetMapping("/AdminHome")
    public String adminHome(HttpSession session){
        String adminemail=(String)session.getAttribute("adminemail");
        if(adminemail==null)return "redirect:/AdminLogin";
        else return "adminhome";
    }
    @GetMapping("/AdminManageCities")
    public String adminManageCities(HttpSession session){
        String adminemail=(String)session.getAttribute("adminemail");
        if(adminemail==null)return "redirect:/AdminLogin";
        else return "adminmanagecities";
    }
    @GetMapping("/AdminManageFranchises")
    public String adminManageFranchises(HttpSession session){
        String adminemail=(String)session.getAttribute("adminemail");
        if(adminemail==null)return "redirect:/AdminLogin";
        else return "adminmanagefranchises";
    }
    
    @GetMapping("/AdminManageGyms")
    public String adminManageGyms(HttpSession session){
        String adminemail=(String)session.getAttribute("adminemail");
        if(adminemail==null)return "redirect:/AdminLogin";
        else return "adminmanagegyms";
    }
    @GetMapping("/AdminLogOut")
    public String adminLogOut(HttpSession session){
        session.removeAttribute("adminemail");
        return "redirect:/";
    }
    @GetMapping("/AdminChangePassword")
    public String adminChangePassword(HttpSession session){
        String adminemail=(String)session.getAttribute("adminemail");
        if(adminemail==null)return "redirect:/AdminLogin";
        return "adminchangepassword";
    }
    @GetMapping("/AdminManageCommission")
    public String adminManageCommission(HttpSession session){
        String adminemail=(String)session.getAttribute("adminemail");
        if(adminemail==null)return "redirect:/AdminLogin";
        return "AdminManageCommission";
    }
    @GetMapping("/CheckEarnings")
    public String checkEarnings(HttpSession session){
        String adminemail=(String)session.getAttribute("adminemail");
        if(adminemail==null)return "redirect:/AdminLogin";
        return "checkearnings";
    }
}
