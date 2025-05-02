/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories;

import java.util.List;
import java.util.Map;

import com.txd.pojo.User;

/**
 *
 * @author tran1
 */
public interface UserRepository {

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    List<User> getUsers(Map<String, String> params);

    long countUsers(Map<String, String> params);

    User getUserById(int id);

    User saveOrUpdate(User user);

    void deleteUser(int id) throws IllegalArgumentException;

    User addUser(User u);

    boolean authenticate(String username, String password);
}
