/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import java.util.List;

/**
 *
 * @author tran1
 */
public class PaymentRequestDTO {

    private String paymentMethod;
    private List<OrderDetailDTO> items;

    /**
     * @return the paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod the paymentMethod to set
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @return the items
     */
    public List<OrderDetailDTO> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<OrderDetailDTO> items) {
        this.items = items;
    }
}
