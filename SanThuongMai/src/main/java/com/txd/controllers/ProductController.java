/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import com.txd.pojo.Product;
import com.txd.service.ProductService;
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

/**
 *
 * @author tran1
 */
@Controller
public class ProductController {
    @Autowired
    private ProductService proSer;
    
    @GetMapping("/products")
    public String showProducts(Model model,@RequestParam Map<String,String> params){
        
        model.addAttribute("products", proSer.getProducts(params));
        return "productsManager/products";
    }
    @GetMapping("/edit-product/{pId}")
    public String editProduct(Model model,@PathVariable("pId") int pId){
        
        
        model.addAttribute("product", proSer.getProductById(pId));
        return "productsManager/editProduct";
    }
    @PostMapping("/updated-product")
    public String updatedProduct(@ModelAttribute("product") Product product){
        
        proSer.saveOrUpdate(product);
        return "redirect:/products";
    }
}
