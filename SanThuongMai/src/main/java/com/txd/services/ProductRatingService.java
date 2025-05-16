/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services;

import com.txd.pojo.Productrating;

/**
 *
 * @author tran1
 */
public interface ProductRatingService {

    Productrating saveOrUpdateRating(Productrating rating);

    Productrating getRatingByCustomerAndProduct(int customerId, int productId);

    Double getAverageRating(int productId);

    Long getRatingCount(int productId);
}
