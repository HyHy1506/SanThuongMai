/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.txd.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.txd.pojo.User;

/**
 *
 * @author tran1
 */
public interface UserService extends UserDetailsService {

    User getUserByUsername(String username);

    User register(Map<String, String> params, MultipartFile avatar);
}
