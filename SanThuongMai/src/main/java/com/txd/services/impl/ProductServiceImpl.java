/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.txd.pojo.Product;
import com.txd.repositories.ProductRepository;
import com.txd.services.ProductService;
import java.util.Date;

/**
 *
 * @author tran1
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository proRepo;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<Product> getProducts(Map<String, String> params) {
        return proRepo.getProducts(params);
    }

    @Override
    public Product saveOrUpdate(Product p) {
        if (!p.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(p.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                p.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(ProductServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (p.getId() == null) {
            p.setCreateAt(new Date());
            p.setUpdateAt(new Date());
            p.setIsActive(true); // Default for new products
        } else {
            p.setUpdateAt(new Date());
            p.setCreateAt(proRepo.getProductById(p.getId()).getCreateAt());
        }
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

    @Override
    public void deleteProduct(int id) {
        proRepo.deleteProduct(id);
    }
}
