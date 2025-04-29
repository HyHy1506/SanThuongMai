/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import com.txd.services.ProductService;
import com.txd.services.CategoryService;
import com.txd.services.PaymentService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class StatisticsController {
     @Autowired
    private ProductService productService;



    @Autowired
    private PaymentService paymentService;

    @GetMapping("/statistics")
    public String showStatistics(Model model, @RequestParam Map<String, String> params) {
    

        
        String period = params.getOrDefault("period", "year");
        String chartType = params.getOrDefault("chartType", "bar");
        String year = params.getOrDefault("year", String.valueOf(java.time.LocalDate.now().getYear()));
        String categoryId = params.get("categoryId");

        model.addAttribute("period", period);
        model.addAttribute("chartType", chartType);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedCategoryId", categoryId != null && !categoryId.isEmpty() ? Integer.valueOf(categoryId) : null);

        return "StatisticsManager/statistics";
    }
}
