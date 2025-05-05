/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.txd.pojo.User;
import com.txd.services.UserService;

/**
 *
 * @author tran1
 */
@Component
public class AuthHelper {

    @Autowired
    private UserService userDetailsService;

    public Map<String, Object> getUsernameFromToken(String authHeader, String requiredRole) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("status", "fail");
                response.put("error", "Thiếu hoặc sai định dạng Authorization header");
                return response;
            }

            String token = authHeader.substring(7);
            String username = JwtUtils.validateTokenAndGetUsername(token);
            if (username == null) {
                response.put("status", "fail");
                response.put("error", "Token không hợp lệ hoặc đã hết hạn");
                return response;
            }

            User user = userDetailsService.getUserByUsername(username);
            if (user == null || (requiredRole != null && !requiredRole.equals(user.getUserRole()))) {
                response.put("status", "fail");
                response.put("error", "Không đủ quyền thực hiện thao tác");
                return response;
            }
            response.put("status", "success");
            response.put("username", username);
            return response;

        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Lỗi xác thực: " + e.getMessage());
            return response;
        }

    }

}
