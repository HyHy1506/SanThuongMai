/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories;

import com.txd.pojo.Admin;

/**
 *
 * @author tran1
 */
public interface AdminRepository {

    void saveOrUpdate(Admin seller);

    void deleteAdmin(int id);
}
