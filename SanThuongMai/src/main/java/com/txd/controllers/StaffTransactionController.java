/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.txd.pojo.Payment;
import com.txd.services.PaymentService;
import com.txd.utils.GlobalVariables;

/**
 *
 * @author tran1
 */
@Controller
@RequestMapping("/staff")
public class StaffTransactionController {

    @Autowired
    private PaymentService paymentService;


    @GetMapping("/transactions")
    public String showTransactionsPay(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = GlobalVariables.PAGE_SIZE;
        long totalPayments = paymentService.countPayments(params);
        int totalPages = (int) Math.ceil((double) totalPayments / pageSize);
        params.compute("page", (k, v) -> String.valueOf(page));
        model.addAttribute("payments", paymentService.getPayments(params));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages > 0 ? totalPages : 1);
        model.addAttribute("selectedPaymentMethod", params.get("paymentMethod"));
        model.addAttribute("selectedIsPay", params.get("isPay"));
        model.addAttribute("selectedIsPayForSeller", params.get("isPayForSeller"));
        return "TransactionsManager/transactions-pay";
    }

 

    @PostMapping("/transactions/mark-paid/{paymentId}")
    public String markAsPaid(@PathVariable("paymentId") int paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment != null && !payment.getIsPay()) {
            payment.setIsPay(true);
            paymentService.save(payment);
        }
        return "redirect:/staff/transactions";
    }

    @PostMapping("/transactions/mark-paid-seller/{paymentId}")
    public String markAsPaidForSeller(@PathVariable("paymentId") int paymentId) {
        paymentService.paySeller(paymentId);
        return "redirect:/staff/transactions";
    }
}
