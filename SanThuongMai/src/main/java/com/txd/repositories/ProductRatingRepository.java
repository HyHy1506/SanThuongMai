/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories;

import com.txd.pojo.Productrating;
import java.util.List;

/**
 *
 * @author tran1
 */
public interface ProductRatingRepository {

    Productrating saveOrUpdateRating(Productrating rating);

    Productrating getRatingByCustomerAndProduct(int customerId, int productId);

    Double getAverageRating(int productId);

    Long getRatingCount(int productId);

    List<Productrating> getRatingsByProduct(int productId);
}
