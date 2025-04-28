/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories;

import java.util.List;
import java.util.Map;

import com.txd.pojo.Seller;

/**
 *
 * @author tran1
 */
public interface SellerRepository {

    List<Seller> getSellers(Map<String, String> params);

    List<Seller> getSellersWithoutShop(Map<String, String> params);

    Seller getSellerById(int id);

    void saveOrUpdate(Seller seller);

    void deleteSeller(int id);

    void updateSellerStatus(Seller seller);
}
