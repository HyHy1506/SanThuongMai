/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import com.txd.pojo.Orderdetail;
import com.txd.pojo.Payment;
import com.txd.pojo.Paymentdetail;
import com.txd.repositories.OrderdetailRepository;
import com.txd.repositories.PaymentRepository;
import com.txd.repositories.PaymentdetailRepository;
import com.txd.services.PaymentService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tran1
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderdetailRepository orderdetailRepository;

    @Autowired
    private PaymentdetailRepository paymentdetailRepository;

    @Override
    public List<Payment> getPayments(Map<String, String> params) {
        return paymentRepository.getPayments(params);
    }

    @Override
    public long countPayments(Map<String, String> params) {
        return paymentRepository.countPayments(params);
    }

    @Override
    public Payment getPaymentById(int id) {
        return paymentRepository.getPaymentById(id);
    }

    @Override
    public void deletePayment(int id) {
        paymentRepository.deletePayment(id);
    }

    @Override
    public Payment createPayment(Payment payment, List<Orderdetail> orderDetails) {
         // Save Payment
        Payment savedPayment = paymentRepository.save(payment);

        // Save OrderDetails va tao PaymentDetails
        for (Orderdetail od : orderDetails) {
            Orderdetail savedOrderDetail = orderdetailRepository.save(od);

            // tao PaymentDetail
            Paymentdetail pd = new Paymentdetail();
            pd.setPaymentId(savedPayment);
            pd.setOrderDetailId(savedOrderDetail);

            paymentdetailRepository.save(pd);
        }

        return savedPayment;
    }
}
