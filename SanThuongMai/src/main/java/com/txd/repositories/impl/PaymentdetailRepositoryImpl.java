package com.txd.repositories.impl;

import com.txd.pojo.Paymentdetail;
import com.txd.repositories.PaymentdetailRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PaymentdetailRepositoryImpl implements PaymentdetailRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Paymentdetail save(Paymentdetail paymentdetail) {
        Session session = this.factory.getObject().getCurrentSession();
        if (paymentdetail.getId() == null) {
            session.persist(paymentdetail);
        } else {
            session.merge(paymentdetail);
        }
        session.flush();
        session.refresh(paymentdetail);
        return paymentdetail;
    }
}