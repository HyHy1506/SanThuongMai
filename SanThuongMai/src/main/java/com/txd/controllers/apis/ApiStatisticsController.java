/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers.apis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.txd.pojo.Category;
import com.txd.pojo.Payment;
import com.txd.pojo.Paymentdetail;
import com.txd.services.PaymentService;

@RestController
@RequestMapping("/api")
public class ApiStatisticsController {
     @Autowired
    private PaymentService paymentService;

    @GetMapping("/statistics/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueStatistics(
            @RequestParam(name = "period") String period,
            @RequestParam(name = "year") int year,
            @RequestParam(name = "categoryId", required = false, defaultValue = "") String sCategoryId) {
        Integer categoryId=null;
        if(sCategoryId!=null && !sCategoryId.isBlank()){
            categoryId=Integer.valueOf(sCategoryId);
        }

        Map<String, Object> response = new HashMap<>();
        List<Payment> payments = paymentService.getPayments(new HashMap<>());

        // Lọc các giao dịch đã thanh toán
        payments = payments.stream()
                .filter(Payment::getIsPay)
                .filter(p -> p.getCreateAt().getYear() + 1900 == year)
                .collect(Collectors.toList());

        if ("month".equals(period)) {
            // Thống kê theo tháng
            Map<String, Double> revenueByMonth = new TreeMap<>();
            for (int i = 1; i <= 12; i++) {
                revenueByMonth.put("Tháng " + i, 0.0);
            }

            for (Payment payment : payments) {
                int month = payment.getCreateAt().getMonth() + 1;
                double revenue = calculateRevenue(payment, categoryId);
                revenueByMonth.compute("Tháng " + month, (k, v) -> v == null ? revenue : v + revenue);
            }

            response.put("labels", new ArrayList<>(revenueByMonth.keySet()));
            response.put("data", new ArrayList<>(revenueByMonth.values()));

        } else if ("quarter".equals(period)) {
            // Thống kê theo quý
            Map<String, Double> revenueByQuarter = new TreeMap<>();
            revenueByQuarter.put("Quý 1", 0.0);
            revenueByQuarter.put("Quý 2", 0.0);
            revenueByQuarter.put("Quý 3", 0.0);
            revenueByQuarter.put("Quý 4", 0.0);

            for (Payment payment : payments) {
                int month = payment.getCreateAt().getMonth() + 1;
                String quarter = "Quý " + ((month - 1) / 3 + 1);
                double revenue = calculateRevenue(payment, categoryId);
                revenueByQuarter.compute(quarter, (k, v) -> v == null ? revenue : v + revenue);
            }

            response.put("labels", new ArrayList<>(revenueByQuarter.keySet()));
            response.put("data", new ArrayList<>(revenueByQuarter.values()));

        } else {
            // Thống kê theo năm (doanh thu các danh mục hoặc sản phẩm)
            Map<String, Double> revenueByCategory = new TreeMap<>();
            for (Payment payment : payments) {
                for (Paymentdetail detail : payment.getPaymentdetailSet()) {
                    if (detail.getOrderDetailId() != null && detail.getOrderDetailId().getProductId() != null) {
                        Category category = detail.getOrderDetailId().getProductId().getCategoryId();
                        if (categoryId == null || category.getId() == categoryId) {
                            String categoryName = category.getName();
                            double revenue;
                            revenue = detail.getOrderDetailId().getPrice()
                                    .multiply(BigDecimal.valueOf( detail.getOrderDetailId().getQuantity())).doubleValue() ;
                            revenueByCategory.compute(categoryName, (k, v) -> v == null ? revenue : v + revenue);
                        }
                    }
                }
            }

            response.put("labels", new ArrayList<>(revenueByCategory.keySet()));
            response.put("data", new ArrayList<>(revenueByCategory.values()));
        }

        return ResponseEntity.ok(response);
    }

    private double calculateRevenue(Payment payment, Integer categoryId) {
        double revenue = 0.0;
        for (Paymentdetail detail : payment.getPaymentdetailSet()) {
            if (detail.getOrderDetailId() != null && detail.getOrderDetailId().getProductId() != null) {
                Category category = detail.getOrderDetailId().getProductId().getCategoryId();
                if (categoryId == null || category.getId() == categoryId) {
                    revenue += detail.getOrderDetailId().getPrice()
                            .multiply(BigDecimal.valueOf(detail.getOrderDetailId().getQuantity())).doubleValue() ;
                }
            }
        }
        return revenue;
    }
}
