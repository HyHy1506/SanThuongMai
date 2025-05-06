/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services;

import com.txd.pojo.Orderdetail;
import com.txd.pojo.Payment;
import java.util.List;
import java.util.Map;

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
}
