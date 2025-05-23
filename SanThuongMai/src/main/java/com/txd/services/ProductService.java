/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.txd.pojo.Product;

/**
 *
 * @author tran1
 */
public interface ProductService {

    List<Product> getProducts(Map<String, String> params);

    Product saveOrUpdate(Product p);

    Product getProductById(int id);

    Long countProducts(Map<String, String> params);

    void deleteProduct(int id);

    Product addProduct(Map<String, Object> params, int sellerId, MultipartFile image);

    Product updateProduct(Map<String, Object> params, int productId, MultipartFile image);

    Integer getSalesQuantity(int productId);
}
