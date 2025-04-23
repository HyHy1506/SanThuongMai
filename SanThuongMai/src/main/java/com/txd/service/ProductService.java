/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.service;

import com.txd.pojo.Product;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tran1
 */
public interface ProductService {
    List<Product> getProducts(Map<String, String> params);
     Product saveOrUpdate(Product p);
     Product getProductById(int id);
}
