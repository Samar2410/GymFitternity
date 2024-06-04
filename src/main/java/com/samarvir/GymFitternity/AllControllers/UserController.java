
package com.samarvir.GymFitternity.AllControllers;

import com.samarvir.GymFitternity.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {
    
    @GetMapping("/")
    public String indexPage(){
        return "index";
    }
    @GetMapping("/UserShowGyms")
    public String userShowGyms(@RequestParam String cityname,Model model,HttpSession session){
        String useremail=(String)session.getAttribute("useremail");
        if(useremail==null)return "redirect:/UserLogin";
        model.addAttribute("cityname",cityname);
        return "usershowgyms";
    }
    
    @GetMapping("/UserShowGymData")
    public String userShowGymData(@RequestParam int id,Model model,HttpSession session){
        String useremail=(String)session.getAttribute("useremail");
        if(useremail==null)return "redirect:/UserLogin";
        model.addAttribute("gymid",id);
        return "UserShowGymData";
    }
     @GetMapping("/UserLogin")
    public String userLogin(){
        
        return "UserLogin";
    }
    @GetMapping("/UserSignup")
    public String userSignup(){
        
        return "UserSignup";
    }
    @GetMapping("/UserLogOut")
    public String userLogout(HttpSession session){
        session.removeAttribute("useremail");
        return "redirect:/";
    }
    
    @GetMapping("/UserChoosePackage")
    public String userChoosePackage(@RequestParam int id,Model model,HttpSession session){
        String useremail=(String)session.getAttribute("useremail");
        if(useremail==null)return "redirect:/UserLogin";
        model.addAttribute("packageid",id);
        return "userselectdate";
    }
    @GetMapping("/PaymentOptions")
    public String paymentOptions(@RequestParam int packageid,@RequestParam String startdate,@RequestParam String enddate,Model model,HttpSession session){
        String useremail=(String)session.getAttribute("useremail");
        if(useremail==null)return "redirect:/UserLogin";
        model.addAttribute("packageid",packageid);
         model.addAttribute("startdate",startdate);
          model.addAttribute("enddate",enddate);
        return "paymentoptions";
    }
    
    @GetMapping("/UserBookings")
    public String userBookings(HttpSession session){
        String useremail=(String)session.getAttribute("useremail");
        if(useremail==null)return "redirect:/UserLogin";
        return "userviewbookings";
    }
    @GetMapping("/UserChangePassword")
    public String userChangePassword(HttpSession session){
        String useremail=(String)session.getAttribute("useremail");
        if(useremail==null)return "redirect:/UserLogin";
        return "userchangepassword";
    }
    
    @GetMapping("/UserForgotPassword")
    public String userForgotPassword(HttpSession session){
        String useremail=(String)session.getAttribute("useremail");
        if(useremail==null)return "redirect:/UserLogin";
        return "UserForgotPassword";
    }
}
