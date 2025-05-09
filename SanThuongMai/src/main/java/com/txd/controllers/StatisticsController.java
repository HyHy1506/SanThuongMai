/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import com.txd.dto.ShopDTO;
import com.txd.pojo.Shop;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.txd.services.PaymentService;
import com.txd.services.ProductService;
import com.txd.services.ShopService;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class StatisticsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;
    @Autowired
    private PaymentService paymentService;
    private static final Logger logger = Logger.getLogger(StatisticsController.class.getName());

    @GetMapping("/statistics/revenue")
    public String showStatisticsRevenue(Model model, @RequestParam Map<String, String> params) {

        String period = params.getOrDefault("period", "year");
        String chartType = params.getOrDefault("chartType", "bar");
        String year = params.getOrDefault("year", String.valueOf(java.time.LocalDate.now().getYear()));
        String categoryId = params.get("categoryId");

        model.addAttribute("period", period);
        model.addAttribute("chartType", chartType);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedCategoryId", categoryId != null && !categoryId.isEmpty() ? Integer.valueOf(categoryId) : null);

        return "StatisticsManager/statistics-revenue";
    }

    @GetMapping("/statistics/frequency")
    public String showStatisticsFrequency(Model model, @RequestParam Map<String, String> params) {
        String period = params.getOrDefault("period", "year");
        String chartType = params.getOrDefault("chartType", "bar");
        String year = params.getOrDefault("year", String.valueOf(java.time.LocalDate.now().getYear()));
        String month = params.get("month");
        String quarter = params.get("quarter");
        String shopId = params.get("shopId");

        List<Shop> shops = this.shopService.getShops(params);
        List<ShopDTO> shopDTOs = shops.stream().map(ShopDTO::new).collect(Collectors.toList());

        model.addAttribute("period", period);
        model.addAttribute("chartType", chartType);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month != null && !month.isEmpty() ? Integer.valueOf(month) : null);
        model.addAttribute("selectedQuarter", quarter != null && !quarter.isEmpty() ? Integer.valueOf(quarter) : null);
        model.addAttribute("selectedShopId", shopId != null && !shopId.isEmpty() ? Integer.valueOf(shopId) : null);
        model.addAttribute("shops", shopDTOs);

        return "StatisticsManager/statistics-frequency";
    }
}
