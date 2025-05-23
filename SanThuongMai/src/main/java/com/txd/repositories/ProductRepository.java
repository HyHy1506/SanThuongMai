/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories;

import java.util.List;
import java.util.Map;

import com.txd.pojo.Product;

/**
 *
 * @author tran1
 */
public interface ProductRepository {

    List<Product> getProducts(Map<String, String> params);

    Product saveOrUpdate(Product p);

    Product getProductById(int id);

    Long countProducts(Map<String, String> params);

    void deleteProduct(int id);

    Integer getSalesQuantity(int productId);
}
