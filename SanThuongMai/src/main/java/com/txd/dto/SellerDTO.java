/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import com.txd.pojo.Seller;

/**
 *
 * @author tran1
 */
public class SellerDTO {

    private Integer id;
    private String status;
    private Integer userId;
    private String nickname;
    private String email;
    private Boolean isActive;
    private String shopName;
    private Integer shopId;

    public SellerDTO(Seller seller) {
        if (seller != null) {
            this.id = seller.getUserId();
            this.status = seller.getStatus().toString();
            this.userId = seller.getUser() != null ? seller.getUser().getId() : null;
            this.nickname = seller.getUser() != null ? seller.getUser().getNickname() : null;
            this.email = seller.getUser() != null ? seller.getUser().getEmail() : null;
            this.isActive = seller.getUser().getIsActive();
            this.shopName= seller.getShop().getName();            
            this.shopId= seller.getShop().getId();

        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * @param shopName the shopName to set
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * @return the shopId
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * @param shopId the shopId to set
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
