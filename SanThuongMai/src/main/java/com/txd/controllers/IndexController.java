/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import com.txd.service.CategoryService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author tran1
 */
@Controller
 @ControllerAdvice
public class IndexController {
    
    
    @Autowired
    private CategoryService cateSer;
    
    @ModelAttribute
    public void commonReponse(Model model){
        model.addAttribute("categories",cateSer.getCates());
    }
    @RequestMapping("/")
     public String index(Model model, @RequestParam Map<String, String> params) {
         
         model.addAttribute("hello", "lan dau");
         return "index";
     }
}
