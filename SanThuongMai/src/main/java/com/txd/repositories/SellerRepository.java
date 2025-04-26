/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories;

import com.txd.pojo.Seller;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tran1
 */
public interface SellerRepository {
    List<Seller> getSeller(Map<String,String> params);
    List<Seller> getSellersWithoutShop(Map<String, String> params);
}
