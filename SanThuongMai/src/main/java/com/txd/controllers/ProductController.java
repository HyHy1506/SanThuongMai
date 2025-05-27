/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.txd.dto.AttributeDTO;
import com.txd.dto.CommentDTO;
import com.txd.dto.ProductDetailDTO;
import com.txd.pojo.Comment;
import com.txd.pojo.Product;
import com.txd.pojo.Productattribute;
import com.txd.services.AttributeService;
import com.txd.services.CommentService;
import com.txd.services.ProductRatingService;
import com.txd.services.ProductService;
import com.txd.services.UserService;
import com.txd.utils.GlobalVariables;

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
    private AttributeService attributeService;
    @Autowired
    private GlobalVariables globalVariables;
    private static final Logger logger = Logger.getLogger(ProductController.class.getName());

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
            return "error"; // tra ve trang loi
        }

        Long commentCount = commentService.getCommentCount(id);
        List<Comment> comments = commentService.getCommentsByProductId(id);
        Integer salesQuantity = proSer.getSalesQuantity(id);

        model.addAttribute("product", new ProductDetailDTO(product));
        List<CommentDTO> commentDTOs = comments.stream().map(CommentDTO::new).collect(Collectors.toList());

        model.addAttribute("commentCount", commentCount);
        model.addAttribute("comments", commentDTOs);
        model.addAttribute("salesQuantity", salesQuantity);

        return "ProductsManager/productDetails";
    }

    @GetMapping("/edit-product/{pId}")
    public String editProduct(Model model, @PathVariable("pId") int pId) {
        List<AttributeDTO> attDtos = attributeService.getAttributes().stream().map(AttributeDTO::new).collect(Collectors.toList());
        model.addAttribute("attributes", attDtos);
        model.addAttribute("product", proSer.getProductById(pId));
        return "ProductsManager/editProduct";
    }

    @PostMapping("/updated-product")
    public String updatedProduct(
            @ModelAttribute("product") Product product,
            @RequestParam Map<String, String> allParams) {

        Set<Productattribute> proSet = new HashSet<>();
        List<Integer> attributeTypeIds = new ArrayList<>();
        List<String> attributeValues = new ArrayList<>();

        // Parse attributeCount để biết cần xử lý bao nhiêu thuộc tính
        int attributeCount = Integer.parseInt(allParams.getOrDefault("attributeCount", "0"));

        // Chuyển đổi allParams thành các list cần thiết
        for (int i = 0; i < attributeCount; i++) {
            String attrTypeKey = "attributeTypeIds[" + i + "]";
            String attrValueKey = "attributeValues[" + i + "]";

            if (allParams.containsKey(attrTypeKey) && allParams.containsKey(attrValueKey)) {
                String typeIdStr = allParams.get(attrTypeKey);
                String valueStr = allParams.get(attrValueKey);

                if (typeIdStr != null && !typeIdStr.isEmpty()) {
                    try {

                        int typeId = Integer.parseInt(typeIdStr);
                        attributeTypeIds.add(typeId);
                        attributeValues.add(valueStr);
                    } catch (NumberFormatException e) {
                        // Bỏ qua nếu typeId không hợp lệ
                    }
                }
            }
        }
        // thêm từng thuộc tính vào sản phẩm
        for (int i = 0; i < attributeTypeIds.size(); i++) {
            if (attributeTypeIds.get(i) != null
                    && attributeValues.get(i) != null && !attributeValues.get(i).trim().isEmpty()) {
                Productattribute newProAttr = new Productattribute(attributeTypeIds.get(i), attributeValues.get(i));
                proSet.add(newProAttr);
            }
        }
        product.setProductattributeSet(proSet);

        Product newP = proSer.saveOrUpdate(product);
        ProductDetailDTO pdDto = new ProductDetailDTO(newP);
        return "redirect:/admin/products";
    }

    private int extractIndex(String key) {
        int start = key.indexOf('[') + 1;
        int end = key.indexOf(']');
        return Integer.parseInt(key.substring(start, end));
    }

    private <T> void ensureListSize(List<T> list, int size) {
        while (list.size() < size) {
            list.add(null);
        }
    }
}
