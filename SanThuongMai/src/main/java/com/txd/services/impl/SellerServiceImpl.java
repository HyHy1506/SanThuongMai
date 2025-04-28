/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txd.pojo.Seller;
import com.txd.repositories.SellerRepository;
import com.txd.services.SellerService;

/**
 *
 * @author tran1
 */
@Service
public class SellerServiceImpl implements SellerService{
    
    @Autowired
    private SellerRepository sellerRepo;
    
    @Override
    public List<Seller> getSellers(Map<String, String> params) {
        return sellerRepo.getSellers(params);
    }

    @Override
    public List<Seller> getSellersWithoutShop(Map<String, String> params) {
        return sellerRepo.getSellersWithoutShop(params);
    }
      @Override
    public Seller getSellerById(int id) {
        return sellerRepo.getSellerById(id);
    }

    @Override
    public void saveOrUpdate(Seller seller) {
        sellerRepo.saveOrUpdate(seller);
    }

    @Override
    public void deleteSeller(int id) {
        sellerRepo.deleteSeller(id);
    }

  
    @Override
    public void updateSellerStatus(Seller seller) {
        sellerRepo.updateSellerStatus(seller);
    }
}
