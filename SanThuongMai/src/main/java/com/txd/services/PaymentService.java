/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services;

import java.util.List;
import java.util.Map;

import com.txd.pojo.Orderdetail;
import com.txd.pojo.Payment;

/**
 *
 * @author tran1
 */
public interface PaymentService {

    List<Payment> getPayments(Map<String, String> params);

    long countPayments(Map<String, String> params);

    Payment getPaymentById(int id);

    void deletePayment(int id);

    Payment createPayment(Payment payment, List<Orderdetail> orderDetails);

    List<Payment> getPaymentsByCustomerId(int customerId);

    List<Map<String, Object>> getSalesFrequencyByShop(Map<String, Object> params);

    List<Map<String, Object>> getTotalProductsSoldByShop(Map<String, Object> params);

    Payment save(Payment payment);

    void paySeller(int paymentId);
}
