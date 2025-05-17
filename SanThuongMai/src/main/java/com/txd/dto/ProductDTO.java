/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import com.txd.pojo.Product;
import com.txd.pojo.Productattribute;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductDTO {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String image;
    private Integer categoryId;
    private String categoryName;
    private Integer shopId;
    private String shopName;    
    private String description;
    private Integer inventoryQuantity;
    private Boolean isActive;

    public ProductDTO(Product product) {
        if (product != null) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.image = product.getImage();            
            this.description = product.getDescription();

            this.categoryId = product.getCategoryId() != null ? product.getCategoryId().getId() : null;
            this.categoryName = product.getCategoryId() != null ? product.getCategoryId().getName() : null;
            this.shopId = product.getShopId() != null ? product.getShopId().getId() : null;
            this.shopName = product.getShopId() != null ? product.getShopId().getName() : null;
            this.isActive = product.getIsActive();
            this.inventoryQuantity=product.getInventoryQuantity();
        }
    }

    // Getters and Setters
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the inventoryQuantity
     */
    public Integer getInventoryQuantity() {
        return inventoryQuantity;
    }

    /**
     * @param inventoryQuantity the inventoryQuantity to set
     */
    public void setInventoryQuantity(Integer inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

}
