/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import com.txd.pojo.Payment;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private Date createAt;
    private List<OrderDetailDTO> orderDetails;

    public PaymentDTO(Payment payment) {
        if (payment != null) {
            this.id = payment.getId();
            this.amount = payment.getPrice();
            this.paymentMethod = payment.getPaymentMethod().toString();
            this.isPay = payment.getIsPay();
            this.createAt = payment.getCreateAt();
            this.customerId = payment.getCustomerId() != null ? payment.getCustomerId().getUserId() : null;
            this.customerNickname = payment.getCustomerId() != null ? payment.getCustomerId().getUser().getNickname() : null;
            if (payment.getOrderdetailSet() != null && !payment.getOrderdetailSet().isEmpty()) {
                this.orderDetails = payment.getOrderdetailSet().stream()
                        .map(Orderdetail -> new OrderDetailDTO(Orderdetail))
                        .collect(Collectors.toList());
            } else {
                this.orderDetails = new ArrayList<>();
            }
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

    /**
     * @return the orderDetails
     */
    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    /**
     * @param orderDetails the orderDetails to set
     */
    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    /**
     * @return the createAt
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt the createAt to set
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
