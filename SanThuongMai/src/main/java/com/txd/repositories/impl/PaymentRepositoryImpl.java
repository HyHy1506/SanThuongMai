/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.txd.pojo.Orderdetail;
import com.txd.pojo.Payment;
import com.txd.pojo.Product;
import com.txd.pojo.Shop;
import com.txd.repositories.PaymentRepository;
import com.txd.utils.GlobalVariables;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
    private static final Logger logger = Logger.getLogger(PaymentRepositoryImpl.class.getName());

    @Override
    public List<Payment> getPayments(Map<String, String> params) {
        int pageSize = GlobalVariables.PAGE_SIZE;
        Session session = sessionFactory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Payment> query = builder.createQuery(Payment.class);
        Root<Payment> root = query.from(Payment.class);

        // Sử dụng fetch join để tải OrderdetailSet, inner để chỉ lấy payment có order :v
        root.fetch("orderdetailSet", JoinType.INNER);
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

        Query q = session.createQuery(query);
        if (params.containsKey("page")) {

            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            q.setFirstResult(start);
            q.setMaxResults(pageSize);

        }
        // chuyển SubList thành  ArrayList
        List<Payment> payments = new ArrayList<>(q.getResultList());
        return payments;

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
    public List<Payment> getPaymentsByCustomerId(int customerId) {
        Session session = sessionFactory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Payment> query = builder.createQuery(Payment.class);
        Root<Payment> root = query.from(Payment.class);
        // Sử dụng fetch join để tải OrderdetailSet, inner để chỉ lấy payment có order :v
        root.fetch("orderdetailSet", JoinType.INNER);
        query.select(root);

        // Thêm điều kiện lọc theo customer_id
        query.where(builder.equal(root.get("customerId").get("userId"), customerId));
        query.orderBy(builder.desc(root.get("id")));
        // Thực thi truy vấn
        return session.createQuery(query).getResultList();
    }

    @Override
    public Payment getPaymentById(int id) {
        Session session = sessionFactory.getObject().getCurrentSession();
        Payment p = session.get(Payment.class, id);
        Hibernate.initialize(p.getOrderdetailSet());
        return p;
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

    @Override
    public List<Map<String, Object>> getSalesFrequencyByShop(Map<String, Object> params) {
        Session session = sessionFactory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<Payment> paymentRoot = query.from(Payment.class);
        Join<Payment, Orderdetail> orderDetailJoin = paymentRoot.join("orderdetailSet", JoinType.INNER);
        Join<Orderdetail, Product> productJoin = orderDetailJoin.join("productId", JoinType.INNER);
        Join<Product, Shop> shopJoin = productJoin.join("shopId", JoinType.INNER);

        // lấy tham số
        String period =(String) params.getOrDefault("period", "year");
        int year = (Integer)params.getOrDefault("year", String.valueOf(java.time.LocalDate.now().getYear()));
        Integer month = params.containsKey("month") ? (Integer)params.get("month") : null;
        Integer quarter = params.containsKey("quarter") ? (Integer)params.get("quarter") : null;
        Integer shopId = (Integer)params.get("shopId");

        // mặc đinh lọc năm
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(paymentRoot.get("isPay"), true));
        predicates.add(builder.equal(builder.function("YEAR", Integer.class, paymentRoot.get("createAt")), year));

        //còn khong thì lọc tháng , quý
        if ("month".equals(period) && month != null) {
            predicates.add(builder.equal(builder.function("MONTH", Integer.class, paymentRoot.get("createAt")), month));
        } else if ("quarter".equals(period) && quarter != null) {
            predicates.add(builder.equal(builder.function("QUARTER", Integer.class, paymentRoot.get("createAt")), quarter));
        }

        // lọc shop
        if (shopId != null ) {
            predicates.add(builder.equal(shopJoin.get("id"),shopId));
        }

        // đếm tổng giao dịch
        query.multiselect(
                shopJoin.get("name").alias("shopName"),
                builder.count(paymentRoot).alias("transactionCount")
        );

        // Group by 
        query.groupBy(shopJoin.get("id"), shopJoin.get("name"));

        // tiến hành where
        query.where(builder.and(predicates.toArray(Predicate[]::new)));

        
        query.orderBy(builder.asc(shopJoin.get("name")));

        //truy lấy thông tin
        List<Object[]> results = session.createQuery(query).getResultList();
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("shopName", row[0]);
            map.put("transactionCount", row[1]);
            resultList.add(map);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getTotalProductsSoldByShop(Map<String, Object> params) {
        Session session = sessionFactory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<Payment> paymentRoot = query.from(Payment.class);
        Join<Payment, Orderdetail> orderDetailJoin = paymentRoot.join("orderdetailSet", JoinType.INNER);
        Join<Orderdetail, Product> productJoin = orderDetailJoin.join("productId", JoinType.INNER);
        Join<Product, Shop> shopJoin = productJoin.join("shopId", JoinType.INNER);

        // lấy tham số
        String period =(String) params.getOrDefault("period", "year");
        int year = (Integer)params.getOrDefault("year", String.valueOf(java.time.LocalDate.now().getYear()));
        Integer month = params.containsKey("month") ? (Integer)params.get("month") : null;
        Integer quarter = params.containsKey("quarter") ? (Integer)params.get("quarter") : null;
        Integer shopId = (Integer)params.get("shopId");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(paymentRoot.get("isPay"), true));
        predicates.add(builder.equal(builder.function("YEAR", Integer.class, paymentRoot.get("createAt")), year));

        if ("month".equals(period) && month != null) {
            predicates.add(builder.equal(builder.function("MONTH", Integer.class, paymentRoot.get("createAt")), month));
        } else if ("quarter".equals(period) && quarter != null) {
            predicates.add(builder.equal(builder.function("QUARTER", Integer.class, paymentRoot.get("createAt")), quarter));
        }

        if (shopId != null ) {
            predicates.add(builder.equal(shopJoin.get("id"), (shopId)));
        }

        query.multiselect(
                shopJoin.get("name").alias("shopName"),
                builder.sum(orderDetailJoin.get("quantity")).alias("totalProducts")
        );

        query.groupBy(shopJoin.get("id"), shopJoin.get("name"));

        query.where(builder.and(predicates.toArray(Predicate[]::new)));

        query.orderBy(builder.asc(shopJoin.get("name")));

        List<Object[]> results = session.createQuery(query).getResultList();
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("shopName", row[0]);
            map.put("totalProducts", row[1]);
            resultList.add(map);
        }
        return resultList;
    }
}
