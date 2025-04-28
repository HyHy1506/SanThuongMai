package com.txd.repositories;

import com.txd.pojo.Customer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tran1
 */
public interface CustomerRepository {

    void saveOrUpdate(Customer customer);

    void deleteCustomer(int id);
}
