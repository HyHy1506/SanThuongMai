/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.txd.pojo.Payment;
import com.txd.repositories.PaymentRepository;
import com.txd.utils.GlobalVariables;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author tran1
 */
@Repository
@Transactional
public class PaymentRepositoryImpl implements PaymentRepository {

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    public List<Payment> getPayments(Map<String, String> params) {

        Session session = sessionFactory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Payment> query = builder.createQuery(Payment.class);
        Root<Payment> root = query.from(Payment.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            //dua vao kw để tìm id hoặc nickname của customer
            if (params.containsKey("kw") && !params.get("kw").isEmpty()) {
                List<Predicate> searchPredicates = new ArrayList<>();

                // Search ID
                String kw = params.get("kw");
                try {
                    int id = Integer.parseInt(kw);
                    searchPredicates.add(builder.equal(root.get("id"), id));
                } catch (NumberFormatException e) {

                }

                // Search nickname
                Join<Payment, Object> customerJoin = root.join("customerId");
                Join<Object, Object> userJoin = customerJoin.join("user");
                searchPredicates.add(builder.like(userJoin.get("nickname"), "%" + kw + "%"));

                predicates.add(builder.or(searchPredicates.toArray(Predicate[]::new)));
            }

            // Filter  method
            if (params.containsKey("paymentMethod") && !params.get("paymentMethod").isEmpty()) {
                predicates.add(builder.equal(root.get("paymentMethod"), params.get("paymentMethod")));
            }

            // Filter status
            if (params.containsKey("isPay") && !params.get("isPay").isEmpty()) {
                predicates.add(builder.equal(root.get("isPay"), Boolean.valueOf(params.get("isPay"))));
            }

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(builder.equal(root.get("isActive"), Boolean.valueOf(isActive)));
            }
        }

        if (!predicates.isEmpty()) {
            query.where(builder.and(predicates.toArray(Predicate[]::new)));
        }

        //xap xep
        String orderBy = params.get("orderBy");
        if (orderBy == null || orderBy.isEmpty() || orderBy.equalsIgnoreCase("desc")) {
            query.orderBy(builder.desc(root.get("id")));
        } else if (orderBy.equalsIgnoreCase("asc")) {
            query.orderBy(builder.asc(root.get("id")));
        }
        query.orderBy(builder.asc(root.get("id")));

//phân trang
        int page = params != null && params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = GlobalVariables.PAGE_SIZE;

        return session.createQuery(query)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public long countPayments(Map<String, String> params) {
        Session session = sessionFactory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Payment> root = query.from(Payment.class);
        query.select(builder.count(root));

        // Build dynamic predicates for filtering
        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {

            if (params.containsKey("kw") && !params.get("kw").isEmpty()) {
                String kw = params.get("kw");
                List<Predicate> searchPredicates = new ArrayList<>();

                try {
                    int id = Integer.parseInt(kw);
                    searchPredicates.add(builder.equal(root.get("id"), id));
                } catch (NumberFormatException e) {
                }

                Join<Payment, Object> customerJoin = root.join("customerId");
                Join<Object, Object> userJoin = customerJoin.join("user");
                searchPredicates.add(builder.like(userJoin.get("nickname"), "%" + kw + "%"));

                predicates.add(builder.or(searchPredicates.toArray(new Predicate[0])));
            }
            //method
            if (params.containsKey("paymentMethod") && !params.get("paymentMethod").isEmpty()) {
                predicates.add(builder.equal(root.get("paymentMethod"), params.get("paymentMethod")));
            }

            //status
            if (params.containsKey("isPay") && !params.get("isPay").isEmpty()) {
                predicates.add(builder.equal(root.get("isPay"), Boolean.parseBoolean(params.get("isPay"))));
            }
        }

        if (!predicates.isEmpty()) {
            query.where(builder.and(predicates.toArray(new Predicate[0])));
        }

        return session.createQuery(query).getSingleResult();
    }

    @Override
    public Payment getPaymentById(int id) {
        Session session = sessionFactory.getObject().getCurrentSession();
        return session.get(Payment.class, id);
    }

    @Override
    public void deletePayment(int id) {
        Session session = sessionFactory.getObject().getCurrentSession();
        Payment payment = session.get(Payment.class, id);
        if (payment != null) {

            //kiem tra con paymetDetail khong 
            // session.delete(payment);
        }
    }
     @Override
    public Payment save(Payment payment) {
        Session session = sessionFactory.getObject().getCurrentSession();
        if (payment.getId() == null) {
            session.persist(payment);
        } else {
            session.merge(payment);
        }
        session.flush();
        session.refresh(payment);
        return payment;
    }

}
