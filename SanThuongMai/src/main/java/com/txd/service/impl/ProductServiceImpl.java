/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.service.impl;

import com.txd.pojo.Product;
import com.txd.repository.ProductRepository;
import com.txd.service.ProductService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tran1
 */
@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository proRepo;
    
    @Override
    public List<Product> getProducts(Map<String, String> params) {
        return proRepo.getProducts(params);
    }

    @Override
    public Product saveOrUpdate(Product p) {
        return proRepo.saveOrUpdate(p);
    }

    @Override
    public Product getProductById(int id) {
        return proRepo.getProductById(id);
    }
     @Override
    public Long countProducts(Map<String, String> params) {
       return proRepo.countProducts(params);
    }
}
