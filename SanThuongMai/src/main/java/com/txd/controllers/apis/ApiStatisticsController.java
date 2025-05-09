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
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.txd.pojo.Category;
import com.txd.pojo.Orderdetail;
import com.txd.pojo.Payment;
import com.txd.pojo.Shop;
import com.txd.services.PaymentService;
import com.txd.services.impl.ProductServiceImpl;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
public class ApiStatisticsController {

    @Autowired
    private PaymentService paymentService;
    private static final Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());

    @GetMapping("/statistics/revenue")
    public ResponseEntity<Map<String, Object>> getRevenueStatistics(
            @RequestParam(name = "period") String period,
            @RequestParam(name = "year") int year,
            @RequestParam(name = "categoryId", required = false, defaultValue = "") String sCategoryId,
            @RequestParam(name = "shopId", required = false, defaultValue = "") String sShopId
    ) {
        Integer categoryId = null;
        Integer shopId = null;

        if (sCategoryId != null && !sCategoryId.isBlank()) {
            categoryId = Integer.valueOf(sCategoryId);
        }
        if (sShopId != null && !sShopId.isBlank()) {
            shopId = Integer.valueOf(sShopId);
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
                double revenue = calculateRevenue(payment, categoryId, shopId);
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
                double revenue = calculateRevenue(payment, categoryId, shopId);
                revenueByQuarter.compute(quarter, (k, v) -> v == null ? revenue : v + revenue);
            }

            response.put("labels", new ArrayList<>(revenueByQuarter.keySet()));
            response.put("data", new ArrayList<>(revenueByQuarter.values()));

        } else {
            // Thống kê theo năm (phân theo các mục hoặc lấy hết)
            Map<String, Double> revenueByCategory = new TreeMap<>();

            ///////////////
             for (Payment payment : payments) {
                for (Orderdetail detail : payment.getOrderdetailSet()) {
                    if (detail.getProductId() != null) {
                        Category category = detail.getProductId().getCategoryId();
                        Shop shop = detail.getProductId().getShopId();
                        if (shopId == null || shop.getId() == shopId) {
                            if (categoryId == null || category.getId() == categoryId) {
                                String categoryName = category.getName();
                                double revenue;
                                revenue = detail.getPrice()
                                        .multiply(BigDecimal.valueOf(detail.getQuantity())).doubleValue();
                                revenueByCategory.compute(categoryName, (k, v) -> v == null ? revenue : v + revenue);
                            }
                        }
                    }
                }
            }

            ////////////////
            response.put("labels", new ArrayList<>(revenueByCategory.keySet()));
            response.put("data", new ArrayList<>(revenueByCategory.values()));

        }

        return ResponseEntity.ok(response);
    }

    private double calculateRevenue(Payment payment, Integer categoryId, Integer shopId) {
        double revenue = 0.0;
        for (Orderdetail detail : payment.getOrderdetailSet()) {
            if (detail.getProductId() != null) {
                Category category = detail.getProductId().getCategoryId();
                Shop shop = detail.getProductId().getShopId();
                if (shopId == null || shop.getId() == shopId) {
                    if (categoryId == null || category.getId() == categoryId) {
                        revenue += detail.getPrice()
                                .multiply(BigDecimal.valueOf(detail.getQuantity())).doubleValue();
                    }
                }

            }
        }

        return revenue;
    }

    @GetMapping("/statistics/frequency/transaction")
    public ResponseEntity<Map<String, Object>> getFrequencyStatistics(
            @RequestParam(name = "period") String period,
            @RequestParam(name = "year") int year,
            @RequestParam(name = "month", required = false) Integer month,
            @RequestParam(name = "quarter", required = false) Integer quarter,
            @RequestParam(name = "shopId", required = false) Integer shopId
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("period", period);
        params.put("year", year);
        params.put("month", month);
        params.put("quarter", quarter);
        params.put("shopId", shopId);
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> fre = paymentService.getSalesFrequencyByShop(params);

            List<String> labels = new ArrayList<>();
            List<Long> data = new ArrayList<>();

            for (Map<String, Object> record : fre) {
                long count = (long) record.get("transactionCount");
                labels.add((String) record.get("shopName"));
                data.add(count);
            }

            response.put("labels", labels);
            response.put("data", data);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("errror", e);

            response.put("status", "fail");
            response.put("data", new ArrayList<>());
            response.put("labels", new ArrayList<>());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }

    }
    @GetMapping("/statistics/frequency/product")
    public ResponseEntity<Map<String, Object>> getTotalStatistics(
            @RequestParam(name = "period") String period,
            @RequestParam(name = "year") int year,
            @RequestParam(name = "month", required = false) Integer month,
            @RequestParam(name = "quarter", required = false) Integer quarter,
            @RequestParam(name = "shopId", required = false) Integer shopId
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("period", period);
        params.put("year", year);
        params.put("month", month);
        params.put("quarter", quarter);
        params.put("shopId", shopId);
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> total = paymentService.getTotalProductsSoldByShop(params);

            List<String> labels = new ArrayList<>();
            List<Integer> data = new ArrayList<>();

            for (Map<String, Object> record : total) {
                int count = (int) record.get("totalProducts");
                labels.add((String) record.get("shopName"));
                data.add(count);
            }

            response.put("labels", labels);
            response.put("data", data);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("errror", e);

            response.put("status", "fail");
            response.put("data", new ArrayList<>());
            response.put("labels", new ArrayList<>());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }

    }
}
