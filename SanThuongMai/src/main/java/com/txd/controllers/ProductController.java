/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import com.txd.dto.CommentDTO;
import com.txd.dto.ProductDTO;
import com.txd.pojo.Comment;
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

import com.txd.pojo.Product;
import com.txd.services.CommentService;
import com.txd.services.ProductRatingService;
import com.txd.services.ProductService;
import com.txd.services.UserService;
import com.txd.utils.GlobalVariables;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author tran1
 */
@Controller
@RequestMapping("/admin")
public class ProductController {

    @Autowired
    private ProductService proSer;
    @Autowired
    private UserService userSer;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ProductRatingService productRatingService;

    @Autowired
    private GlobalVariables globalVariables;

    @GetMapping("/products")
    public String showProducts(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = globalVariables.PAGE_SIZE;
        long totalProducts = proSer.countProducts(params);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        String categoryId = params.get("categoryId");

        model.addAttribute("products", proSer.getProducts(params));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages > 0 ? totalPages : 1);

        //lay danh muc da chon
        model.addAttribute("selectedCategoryId",
                categoryId != null && !categoryId.isEmpty() ? Integer.parseInt(categoryId) : null);
        return "ProductsManager/products";
    }

    @GetMapping("/products/{id}")
    public String productDetails(Model model, @PathVariable("id") int id) {
        Product product = proSer.getProductById(id);
        if (product == null) {
            return "error"; // Redirect to an error page if product not found
        }
        Double averageRating = productRatingService.getAverageRating(id);
        Long ratingCount = productRatingService.getRatingCount(id);
        Long commentCount = commentService.getCommentCount(id);
        List<Comment> comments = commentService.getCommentsByProductId(id);
        
        model.addAttribute("product", new ProductDTO(product));
         List<CommentDTO> commentDTOs = comments.stream().map(CommentDTO::new).collect(Collectors.toList());
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("ratingCount", ratingCount);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("comments", commentDTOs);

        return "ProductsManager/productDetails";
    }

    @GetMapping("/edit-product/{pId}")
    public String editProduct(Model model, @PathVariable("pId") int pId) {

        model.addAttribute("product", proSer.getProductById(pId));
        return "ProductsManager/editProduct";
    }

    @PostMapping("/updated-product")
    public String updatedProduct(@ModelAttribute("product") Product product) {

        proSer.saveOrUpdate(product);
        return "redirect:/admin/products";
    }
}
