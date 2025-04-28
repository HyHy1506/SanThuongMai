/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers;

import com.txd.services.PaymentService;
import com.txd.utils.GlobalVariables;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author tran1
 */
@Controller
@RequestMapping("/admin")
public class TransactionController {

    @Autowired
    private PaymentService paymentService;


    @GetMapping("/transactions")
    public String showTransactions(Model model, @RequestParam Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = GlobalVariables.PAGE_SIZE;
        long totalPayments = paymentService.countPayments(params);
        int totalPages = (int) Math.ceil((double) totalPayments / pageSize);

        model.addAttribute("payments", paymentService.getPayments(params));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages > 0 ? totalPages : 1);
        model.addAttribute("selectedPaymentMethod", params.get("paymentMethod"));
        model.addAttribute("selectedIsPay", params.get("isPay"));

        return "TransactionsManager/transactions";
    }

    @GetMapping("/transactions/{paymentId}")
    public String viewTransaction(Model model, @PathVariable("paymentId") int paymentId) {
        model.addAttribute("payment", paymentService.getPaymentById(paymentId));
        return "TransactionsManager/transaction-details";
    }
}