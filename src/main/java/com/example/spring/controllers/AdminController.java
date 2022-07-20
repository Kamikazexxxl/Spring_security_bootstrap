package com.example.spring.controllers;

import com.example.spring.models.Role;
import com.example.spring.models.User;
import com.example.spring.services.RoleServiceInterface;
import com.example.spring.services.UserService;
import com.example.spring.services.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceInterface userService;
    private final RoleServiceInterface roleService;

    @Autowired
    public AdminController(UserServiceInterface userService, RoleServiceInterface roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String admin(Model model, Principal principal) {
        List<User> list = userService.findAll();
        model.addAttribute("Users", list);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", userService.findUserByUserName(principal.getName()));
        model.addAttribute("newUser", new User());
        return "mainPage";
    }

    @PostMapping(value = "/add")
    public String addNewUser(@ModelAttribute("newUser") User user, @RequestParam(value = "userRole", required = false) ArrayList<Role> roles) {
        user.setRoles(new HashSet<>(roles));
        userService.save(user);
        return "redirect:/admin/";
    }

    @PostMapping(value = "/edit/{id}")
    public String editUser(@PathVariable(name = "id") long id, @ModelAttribute(value = "user") User user,
                           @RequestParam(value = "userRole", required = false) ArrayList<Role> roles) {
        user.setRoles(new HashSet<>(roles));
        userService.edit(user);
        return "redirect:/admin/";
    }

    @PostMapping(value = "/delete/{id}")
    public String remove(@PathVariable(name = "id") long id) {
        userService.remove(id);
        return "redirect:/admin/";
    }

}
