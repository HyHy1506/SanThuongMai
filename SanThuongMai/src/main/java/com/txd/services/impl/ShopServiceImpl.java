/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import com.txd.pojo.Shop;
import com.txd.repositories.ShopRepository;
import com.txd.services.ShopService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tran1
 */
@Service
public class ShopServiceImpl implements ShopService{

    @Autowired
    private ShopRepository shopRepo;
    @Override
    public List<Shop> getShops(Map<String, String> params) {
        return shopRepo.getShops(params);
    }

    @Override
    public Shop getShopById(int id) {
        return shopRepo.getShopById(id);
    }

    @Override
    public void saveOrUpdate(Shop shop) {
        if (shop.getId() == null) {
            shop.setCreateAt(new Date());
            shop.setUpdateAt(new Date());
            shop.setIsActive(true); // Default for new products
        } else {
            shop.setUpdateAt(new Date());
            shop.setCreateAt(shopRepo.getShopById(shop.getId()).getCreateAt());
        }
        shopRepo.saveOrUpdate(shop);
    }

    @Override
    public void deleteShop(int id) {
        shopRepo.deleteShop(id);
    }

    @Override
    public Long countShops(Map<String, String> params) {
        return shopRepo.countShops(params);
    }
    
}
