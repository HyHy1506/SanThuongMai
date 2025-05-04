/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers.apis;

import com.txd.dto.SellerDTO;
import com.txd.pojo.Seller;
import com.txd.pojo.User;
import com.txd.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tran1
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiSellerController {
    @Autowired
    private SellerService sellerService;
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Object> single(@PathVariable(value = "sellerId") int sellerId) {
        Seller s= sellerService.getSellerById(sellerId);
        SellerDTO sellerDTO=new SellerDTO(s);
        return new ResponseEntity<>(sellerDTO, HttpStatus.OK);
    }
}
