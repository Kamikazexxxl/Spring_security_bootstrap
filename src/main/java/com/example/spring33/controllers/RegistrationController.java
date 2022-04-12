package com.example.spring33.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private UserDetails userDetails;

    @Autowired
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @GetMapping("/login")
    public String toLoginPage(Model model) {
        model.addAttribute("userDetails", userDetails);
        return "login";
    }

//    @PostMapping("/login")
//    public String registration(@ModelAttribute("userDetails") UserDetails userDetails) {
//
//        return "login";
//    }
}
