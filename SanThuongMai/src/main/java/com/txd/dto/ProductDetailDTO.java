/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.txd.pojo.Product;
import com.txd.pojo.Productattribute;
import com.txd.pojo.Productrating;

/**
 *
 * @author tran1
 */
public class ProductDetailDTO {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String image;
    private Integer categoryId;
    private String categoryName;
    private Integer shopId;
    private String shopName;
    private Boolean isActive;
    private Map<String, String> attributes = new HashMap<>();
    private Double averageRating=0.0;
    private Integer totalRatings=0;

    public ProductDetailDTO(Product product) {
        if (product != null) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.image = product.getImage();
            this.categoryId = product.getCategoryId() != null ? product.getCategoryId().getId() : null;
            this.categoryName = product.getCategoryId() != null ? product.getCategoryId().getName() : null;
            this.shopId = product.getShopId() != null ? product.getShopId().getId() : null;
            this.shopName = product.getShopId() != null ? product.getShopId().getName() : null;
            if (product.getProductattributeSet() != null) {
                for (Productattribute pa : product.getProductattributeSet()) {
                    if (pa.getAttributeId() != null) {
                        this.attributes.put(pa.getAttributeId().getName(), pa.getValue());
                    }
                }
            }
            this.isActive = product.getIsActive();

            
            if (product.getProductratingSet() != null && !product.getProductratingSet().isEmpty()) {
                this.totalRatings = product.getProductratingSet().size();
                double sum = 0.0;
                for (Productrating pr : product.getProductratingSet()) {
                    if (pr.getRate() != null) {
                        sum += Integer.parseInt(pr.getRate());
                    }
                }
                this.averageRating = sum / this.totalRatings;
            } else {
                this.totalRatings = 0;
                this.averageRating = 0.0;
            }
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
     * @return the attributes
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the averageRating
     */
    public Double getAverageRating() {
        return averageRating;
    }

    /**
     * @param averageRating the averageRating to set
     */
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * @return the totalRatings
     */
    public Integer getTotalRatings() {
        return totalRatings;
    }

    /**
     * @param totalRatings the totalRatings to set
     */
    public void setTotalRatings(Integer totalRatings) {
        this.totalRatings = totalRatings;
    }
}
