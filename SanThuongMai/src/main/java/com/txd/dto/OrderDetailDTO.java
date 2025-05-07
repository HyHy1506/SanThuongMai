/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import com.txd.pojo.Orderdetail;

/**
 *
 * @author tran1
 */
public class OrderDetailDTO {

    private int productId;
    private String name;
    private double price;
    private String image;
    private int quantity;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(int productId, String name, double price, String image, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public OrderDetailDTO(Orderdetail orderDetail) {
        if (orderDetail != null) {
            this.productId = orderDetail.getProductId() != null ? orderDetail.getProductId().getId() : null;
            this.quantity = orderDetail.getQuantity(); 
            this.image = orderDetail.getProductId().getImage();
            this.name = orderDetail.getProductId().getName();
            this.price = orderDetail.getPrice().doubleValue();
        }
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
