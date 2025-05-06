package com.txd.repositories.impl;

import com.txd.pojo.Orderdetail;
import com.txd.repositories.OrderdetailRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OrderdetailRepositoryImpl implements OrderdetailRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Orderdetail save(Orderdetail orderdetail) {
        Session session = this.factory.getObject().getCurrentSession();
        if (orderdetail.getId() == null) {
            session.persist(orderdetail);
        } else {
            session.merge(orderdetail);
        }
        session.flush();
        session.refresh(orderdetail);
        return orderdetail;
    }
}  