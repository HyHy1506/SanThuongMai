/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import com.txd.pojo.Seller;
import com.txd.repositories.SellerRepository;
import com.txd.services.SellerService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tran1
 */
@Service
public class SellerServiceImpl implements SellerService{
    
    @Autowired
    private SellerRepository sellerRepo;
    
    @Override
    public List<Seller> getSeller(Map<String, String> params) {
        return sellerRepo.getSeller(params);
    }

    @Override
    public List<Seller> getSellersWithoutShop(Map<String, String> params) {
        return sellerRepo.getSellersWithoutShop(params);
    }
    
}
