/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import com.txd.pojo.Productrating;
import com.txd.repositories.ProductRatingRepository;
import com.txd.services.ProductRatingService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductRatingServiceImpl implements ProductRatingService{

    @Autowired
    private ProductRatingRepository productRatingRepository;

    @Override
    public Productrating saveOrUpdateRating(Productrating rating) {
        if (rating.getId() == null) {
            rating.setCreateAt(new Date());
            rating.setIsActive(true);
        }
        rating.setUpdateAt(new Date());
        return productRatingRepository.saveOrUpdateRating(rating);
    }

    @Override
    public Productrating getRatingByCustomerAndProduct(int customerId, int productId) {
        return productRatingRepository.getRatingByCustomerAndProduct(customerId, productId);
    }
}
