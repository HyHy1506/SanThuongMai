/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories;

import com.txd.pojo.User;

/**
 *
 * @author tran1
 */
public interface UserRepository {

    User getUserByUsername(String username);

    User register(User u);
}
