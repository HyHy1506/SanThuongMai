/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.txd.pojo.Seller;
import com.txd.services.SellerService;

/**
 *
 * @author tran1
 */
@Controller
@RequestMapping("/staff")
public class CheckController {

    @Autowired
    private SellerService sellerService;

    @GetMapping("/check")
    public String showSellers(Model model) {
        // Prepare parameters for filtering by status
        Map<String, String> pendingParams = new HashMap<>();
        pendingParams.put("status", "PENDING");
        pendingParams.put("orderBy", "desc");
        Map<String, String> approvedParams = new HashMap<>();
        approvedParams.put("status", "APPROVED");
        approvedParams.put("orderBy", "desc");
        Map<String, String> rejectedParams = new HashMap<>();
        rejectedParams.put("status", "REJECT");
        rejectedParams.put("orderBy", "desc");

        // Add seller lists to model
        model.addAttribute("pendingSellers", sellerService.getSellers(pendingParams));
        model.addAttribute("approvedSellers", sellerService.getSellers(approvedParams));
        model.addAttribute("rejectedSellers", sellerService.getSellers(rejectedParams));

        return "CheckManager/check";
    }

    @GetMapping("/check/{sellerId}")
    public String editSeller(Model model, @PathVariable("sellerId") int sellerId) {
        Seller seller = sellerService.getSellerById(sellerId);
        model.addAttribute("seller", seller);
        return "CheckManager/check-form";
    }

    @PostMapping("/update-seller")
    public String updateSeller(@ModelAttribute("seller") Seller seller) {
        sellerService.updateSellerStatus(seller);
        return "redirect:/staff/check";
    }
}
