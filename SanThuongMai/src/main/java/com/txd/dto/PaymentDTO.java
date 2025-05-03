/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import com.txd.pojo.Payment;
import java.math.BigDecimal;

/**
 *
 * @author tran1
 */
public class PaymentDTO {
    private Integer id;
    private BigDecimal amount;
    private String paymentMethod;
    private Boolean isPay;
    private Integer customerId;
    private String customerNickname;

    public PaymentDTO(Payment payment) {
        if (payment != null) {
            this.id = payment.getId();
            this.amount = payment.getPrice();
            this.paymentMethod = payment.getPaymentMethod().toString();
            this.isPay = payment.getIsPay();
            this.customerId = payment.getCustomerId() != null ? payment.getCustomerId().getUserId() : null;
            this.customerNickname = payment.getCustomerId()!= null ? payment.getCustomerId().getUser().getNickname() : null;
        }
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean isPay) {
        this.isPay = isPay;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNickname() {
        return customerNickname;
    }

    public void setCustomerNickname(String customerNickname) {
        this.customerNickname = customerNickname;
    }
}
