/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import com.txd.pojo.Seller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.txd.pojo.Shop;
import com.txd.pojo.User;
import com.txd.services.SellerService;
import com.txd.services.ShopService;
import com.txd.utils.GlobalVariables;
import java.util.HashMap;

/**
 *
 * @author tran1
 */
@Controller
public class ShopController {

    @Autowired
    private GlobalVariables globalVariables;

    @Autowired
    private ShopService shopSer;
    @Autowired
    private SellerService sellerSer;

    @GetMapping("/shops")
    public String showShops(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = globalVariables.PAGE_SIZE;
        long totalProducts = shopSer.countShops(params);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        model.addAttribute("shops", shopSer.getShops(params));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages > 0 ? totalPages : 1);

        return "shopsManager/shops";
    }

    @PostMapping("/update-shop")
    public String add(@ModelAttribute(value = "shop") Shop s) {
        this.shopSer.saveOrUpdate(s);

        return "redirect:/shops";
    }

    @GetMapping("/shops/{shopId}")
    public String updateView(Model model, @PathVariable(value = "shopId") int id) {
        Shop s = shopSer.getShopById(id);
        model.addAttribute("shop", s);
        return "shopsManager/shop-form";
    }

    @GetMapping("/add-shop")
    public String addShop(Model model) {

        model.addAttribute("shop", new Shop());
        Map<String, String> params = new HashMap<>();
        params.put("isActive", "true");
        model.addAttribute("sellers", sellerSer.getSellersWithoutShop(params));
        return "shopsManager/shop-form";
    }
}
