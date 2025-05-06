/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers.apis;

import com.txd.dto.UserDTO;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.txd.pojo.User;
import com.txd.services.UserService;
import com.txd.utils.AuthHelper;
import com.txd.utils.JwtUtils;
import java.security.Principal;

import java.util.Collections;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author tran1
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {

    @Autowired
    private UserService userDetailsService;
    @Autowired
    private AuthHelper authHelper;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User u) {
        Map<String, Object> response = new HashMap<String, Object>();
        if (this.userDetailsService.authenticate(u.getUsername(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername());
                response.compute("status", (k, v) -> "success");
                response.compute("token", (k, v) -> token);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception e) {
                response.compute("status", (k, v) -> "fail");
                response.compute("error", (k, v) -> "Loi tao toke JWT");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        response.compute("status", (k, v) -> "fail");
        response.compute("error", (k, v) -> "Sai thông tin đăng nhập");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(path = "/users",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> create(
            @RequestParam Map<String, String> params, 
            @RequestParam(value = "avatar") MultipartFile avatar) {
        Map<String, Object> response = new HashMap<>();
        //kiem tra user name
        User user1 = userDetailsService.getUserByUsername(params.get("username"));
        if (user1 != null) {
            response.put("status", "fail");
            response.put("error", "Username đã tồn tại");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        //kiem tra email
        User user2 = userDetailsService.getUserByEmail(params.get("email"));
        if (user2 != null) {
            response.put("status", "fail");
            response.put("error", "Email đã tồn tại");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 Conflict
        }

        //neu user khong bi xung dot du lieu thi tao user moi
        try {
            User newUser = userDetailsService.addUser(params, avatar);
            UserDTO userDto = new UserDTO(newUser);
            response.put("status", "success");
            response.put("user", userDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Lỗi khi tạo user: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @GetMapping("/secure/profile")
    @CrossOrigin
    public ResponseEntity<Object> getProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> authResult = authHelper.getUsernameFromToken(authHeader,null);
        if (!"success".equals(authResult.get("status"))) {
            return new ResponseEntity<>(authResult, HttpStatus.UNAUTHORIZED);
        }
        try {
            String username = (String) authResult.get("username");
            User user = userDetailsService.getUserByUsername(username);
            UserDTO userDTO = new UserDTO(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Lỗi xac thuc user: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, String>> destroy(@PathVariable("userId") int id) {
        try {
            this.userDetailsService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "xóa thành công");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Một lỗi ko mong đợi :v : " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/users/{userId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable("userId") int userId,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "avatar",required = false) MultipartFile avatar,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> authResult = authHelper.getUsernameFromToken(authHeader, null);
        if (!"success".equals(authResult.get("status"))) {
            return new ResponseEntity<>(authResult, HttpStatus.UNAUTHORIZED);
        }
        //neu user khong bi xung dot du lieu thi tao user moi
        try {
            String username = (String) authResult.get("username");
            User user = userDetailsService.getUserByUsername(username);
            User newUser = userDetailsService.updateUser(params, avatar,user);
            UserDTO userDto = new UserDTO(newUser);
            response.put("status", "success");
            response.put("user", userDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Lỗi khi tạo user: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

}
