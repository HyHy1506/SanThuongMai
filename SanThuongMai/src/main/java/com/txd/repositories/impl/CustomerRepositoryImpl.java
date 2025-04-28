/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories.impl;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.txd.pojo.Customer;
import com.txd.repositories.CustomerRepository;

/**
 *
 * @author tran1
 */
@Repository
@Transactional
public class CustomerRepositoryImpl implements CustomerRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void saveOrUpdate(Customer customer) {
        Session session = factory.getObject().getCurrentSession();
        if (customer.getUserId() == null) {
            session.persist(customer);
        } else {

            session.merge(customer);
        }
    }

    @Override
    public void deleteCustomer(int id) {
        Session session = factory.getObject().getCurrentSession();
        Customer customer = session.get(Customer.class, id);
        if (customer != null) {
            session.remove(customer);
        }
    }
}
