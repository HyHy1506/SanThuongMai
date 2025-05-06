package com.txd.controllers.apis;

import com.txd.dto.OrderDetailDTO;
import com.txd.dto.PaymentRequestDTO;
import com.txd.pojo.Customer;
import com.txd.pojo.Orderdetail;
import com.txd.pojo.Payment;
import com.txd.pojo.Product;
import com.txd.pojo.User;
import com.txd.services.PaymentService;
import com.txd.services.UserService;
import com.txd.utils.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiPaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthHelper authHelper;

    @PostMapping("/payments")
    public ResponseEntity<Map<String, Object>> createPayment(
            @RequestBody PaymentRequestDTO paymentRequest,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> authResult = authHelper.getUsernameFromToken(authHeader, "Customer");
        if (!"success".equals(authResult.get("status"))) {
            return new ResponseEntity<>(authResult, HttpStatus.UNAUTHORIZED);
        }

        try {
            String username = (String) authResult.get("username");
            User user = userService.getUserByUsername(username);

            // tao  hoa don
            Payment payment = new Payment();
            payment.setCustomerId(new Customer(user.getId()));
            payment.setIsPay(true);
            payment.setPaymentMethod(Payment.PaymentMethodEnum.valueOf(paymentRequest.getPaymentMethod()));

            // tinh tong so tien va tao orderdetail
            List<Orderdetail> orderDetailEntities = paymentRequest.getItems().stream().map(dto -> {
                Orderdetail od = new Orderdetail();
                od.setCustomerId(new Customer(user.getId()));
                od.setProductId(new Product(dto.getProductId()));
                od.setQuantity(dto.getQuantity());
                od.setPrice(new BigDecimal(dto.getPrice()));

                return od;
            }).collect(Collectors.toList());
            
            // tinh tong so tien trong orderDetailEntities
            double totalAmount = orderDetailEntities.stream().mapToDouble(od
                    -> (od.getPrice().multiply(BigDecimal.valueOf(od.getQuantity()))).doubleValue())
                    .sum();
            payment.setPrice(BigDecimal.valueOf(totalAmount));

            // luu ca 3 Payment, OrderDetails, PaymentDetails
            Payment savedPayment = paymentService.createPayment(payment, orderDetailEntities);

            response.put("status", "success");
            response.put("paymentId", savedPayment.getId());
            response.put("totalAmount", totalAmount);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Error creating payment: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
