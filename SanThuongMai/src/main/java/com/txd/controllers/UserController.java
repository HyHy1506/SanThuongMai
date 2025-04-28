/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.txd.pojo.User;
import com.txd.services.UserService;
import com.txd.utils.GlobalVariables;

@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private GlobalVariables globalVariables;

    @GetMapping("/users")
    public String showUsers(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = globalVariables.PAGE_SIZE;
        long totalUsers = userService.countUsers(params);
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
        String selectedIsActive = params.get("isActive");

        model.addAttribute("selectedIsActive",
                selectedIsActive != null && !selectedIsActive.isEmpty() ? selectedIsActive : null);
        model.addAttribute("users", userService.getUsers(params));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages > 0 ? totalPages : 1);

        return "UsersManager/users";
    }

    @GetMapping("/add-user")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "UsersManager/user-form";
    }

    @GetMapping("/users/{userId}")
    public String editUser(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("user", userService.getUserById(userId));
        return "UsersManager/user-form";
    }

    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.saveOrUpdate(user);
        return "redirect:/admin/users";
    }

}
