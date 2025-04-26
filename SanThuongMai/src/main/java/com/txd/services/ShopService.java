/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services;

import com.txd.pojo.Shop;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tran1
 */
public interface ShopService {

    List<Shop> getShops(Map<String, String> params);

    Shop getShopById(int id);

    void saveOrUpdate(Shop shop);

    Long countShops(Map<String, String> params);

    void deleteShop(int id);
}
