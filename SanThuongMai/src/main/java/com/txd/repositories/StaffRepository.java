/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories;

import com.txd.pojo.Staff;

/**
 *
 * @author tran1
 */
public interface StaffRepository {

    void saveOrUpdate(Staff staff);

    void deleteStaff(int id);
}
