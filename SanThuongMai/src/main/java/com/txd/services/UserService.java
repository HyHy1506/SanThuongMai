/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.txd.services;

import java.util.List;
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

    User getUserByEmail(String email);

    List<User> getUsers(Map<String, String> params);

    long countUsers(Map<String, String> params);

    User getUserById(int id);

    void saveOrUpdate(User user);

    void deleteUser(int id) throws IllegalArgumentException;

    User addUser(Map<String, String> params, MultipartFile avatar);

    User updateUser(Map<String, String> params, MultipartFile avatar,User u);

    boolean authenticate(String username, String password);
}
