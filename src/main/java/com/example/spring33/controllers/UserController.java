package com.example.spring33.controllers;

import com.example.spring33.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal) {
        model.addAttribute("Users", List.of(userService.findUserByUserName(principal.getName())));
        model.addAttribute("principal",principal);
        return "panel";
    }
    @GetMapping("/panel")
    public String test(Model model, Principal principal) {
        model.addAttribute("principal",principal);
        return "test";
    }
}
