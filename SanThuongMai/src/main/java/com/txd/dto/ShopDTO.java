/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import com.txd.pojo.Shop;

/**
 *
 * @author tran1
 */
public class ShopDTO {

    private Integer id;
    private String name;
    private Integer sellerId;
    private String sellerNickname;
    private Boolean isActive;

    public ShopDTO(Shop shop) {
        if (shop != null) {
            this.id = shop.getId();
            this.name = shop.getName();
            this.sellerId = shop.getSellerId()!= null ? shop.getSellerId().getUserId(): null;
            this.sellerNickname = shop.getSellerId()!= null ? shop.getSellerId().getUser().getNickname() : null;
            this.isActive = shop.getIsActive();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerNickname() {
        return sellerNickname;
    }

    public void setSellerNickname(String sellerNickname) {
        this.sellerNickname = sellerNickname;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
