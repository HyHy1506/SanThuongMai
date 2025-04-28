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

import com.txd.pojo.Staff;
import com.txd.repositories.StaffRepository;

/**
 *
 * @author tran1
 */
@Repository
@Transactional
public class StaffRepositoryImpl implements StaffRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    @Override
    public void saveOrUpdate(Staff staff) {
        Session session = factory.getObject().getCurrentSession();
        if (staff.getUserId() == null) {
            session.persist(staff);
        } else {

            session.merge(staff);
        }
    }

    @Override
    public void deleteStaff(int id) {
        Session session = factory.getObject().getCurrentSession();
        Staff staff = session.get(Staff.class, id);
        if (staff != null) {
            session.remove(staff);
        }
    }
    
}
