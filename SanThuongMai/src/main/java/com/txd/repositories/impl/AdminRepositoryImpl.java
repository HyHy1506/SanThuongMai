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

import com.txd.pojo.Admin;
import com.txd.repositories.AdminRepository;

/**
 *
 * @author tran1
 */
@Repository
@Transactional
public class AdminRepositoryImpl implements AdminRepository{
     @Autowired
    private LocalSessionFactoryBean factory;
    @Override
    public void saveOrUpdate(Admin admin) {
        Session session = factory.getObject().getCurrentSession();
        if (admin.getUserId() == null) {
            session.persist(admin);
        } else {

            session.merge(admin);
        }
    }

    @Override
    public void deleteAdmin(int id) {
        Session session = factory.getObject().getCurrentSession();
        Admin admin = session.get(Admin.class, id);
        if (admin != null) {
            session.remove(admin);
        }
    }
}
