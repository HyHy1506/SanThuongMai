/*
 * Click nb:fs://nb:fs/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nb:fs://nb:fs/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers.apis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.txd.pojo.Customer;
import com.txd.pojo.Product;
import com.txd.pojo.Productrating;
import com.txd.pojo.User;
import com.txd.services.ProductRatingService;
import com.txd.services.UserService;
import com.txd.utils.AuthHelper;

/**
 *
 * @author tran1
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiProductRatingController {

    @Autowired
    private ProductRatingService productRatingService;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private AuthHelper authHelper;

    @PostMapping("/product-ratings")
    public ResponseEntity<Map<String, Object>> createOrUpdateRating(
            @RequestBody Map<String, Object> params,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> authResult = authHelper.getUsernameFromToken(authHeader, "Customer");
        if (!"success".equals(authResult.get("status"))) {
            return new ResponseEntity<>(authResult, HttpStatus.UNAUTHORIZED);
        }
        try {
            String username = (String) authResult.get("username");
            User user = userDetailsService.getUserByUsername(username);

            Integer productId = (Integer) params.get("productId");
            String ratingValue = (String) params.get("rating");
            if (productId == null || ratingValue == null) {
                response.put("status", "fail");
                response.put("error", "Thiếu productId hoặc rating");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // RatingEnum rating;
            try {

                Integer rateNumber = Integer.valueOf(ratingValue);
                if (rateNumber < 1 || rateNumber > 5) {
                    response.put("status", "fail");
                    response.put("error", "Giá trị rating không chuẩn");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } catch (IllegalArgumentException e) {
                response.put("status", "fail");
                response.put("error", "Giá trị rating lỗi");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Productrating existingRating = productRatingService.getRatingByCustomerAndProduct(user.getId(), productId);
            Productrating productRating = existingRating != null ? existingRating : new Productrating();
            productRating.setProductId(new Product(productId));
            productRating.setCustomerId(new Customer(user.getId()));
            productRating.setRate(ratingValue);

            Productrating savedRating = productRatingService.saveOrUpdateRating(productRating);
            response.put("status", "success");
            response.put("rating", savedRating.getRate());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Lỗi khi lưu rating: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
