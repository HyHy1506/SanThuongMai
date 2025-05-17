/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txd.pojo.Orderdetail;
import com.txd.pojo.Payment;
import com.txd.pojo.Seller;
import com.txd.repositories.OrderdetailRepository;
import com.txd.repositories.PaymentRepository;
import com.txd.repositories.PaymentdetailRepository;
import com.txd.repositories.SellerRepository;
import com.txd.services.PaymentService;

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
    @Autowired
    private SellerRepository sellerRepository;

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
            od.setPaymentId(savedPayment);
            Orderdetail savedOrderDetail = orderdetailRepository.save(od);

        }

        return savedPayment;
    }

    @Override
    public List<Payment> getPaymentsByCustomerId(int customerId) {
        return paymentRepository.getPaymentsByCustomerId(customerId);
    }

    @Override
    public List<Map<String, Object>> getSalesFrequencyByShop(Map<String, Object> params) {
        return paymentRepository.getSalesFrequencyByShop(params);
    }

    @Override
    public List<Map<String, Object>> getTotalProductsSoldByShop(Map<String, Object> params) {
        return paymentRepository.getTotalProductsSoldByShop(params);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void paySeller(int paymentId) {
        Payment payment = paymentRepository.getPaymentById(paymentId);
        if (payment != null && payment.getIsPay() && !payment.getIsPayForSeller()) {
            BigDecimal totalAmount = payment.getPrice();
            BigDecimal platformFee = totalAmount.multiply(new BigDecimal("0.15")); // 15% fee
            BigDecimal sellerAmount = totalAmount.subtract(platformFee);

            // Find the seller associated with the shop of the products in order details
            for (Orderdetail od : payment.getOrderdetailSet()) {
                Seller seller = sellerRepository.findByShopId(od.getProductId().getShopId().getId());
                if (seller != null) {
                    BigDecimal currentBalance = seller.getAccountBalance() != null ? seller.getAccountBalance() : BigDecimal.ZERO;
                    seller.setAccountBalance(currentBalance.add(sellerAmount));
                    sellerRepository.saveOrUpdate(seller);
                }
            }

            payment.setIsPayForSeller(true);
            paymentRepository.save(payment);
        }
    }
}
