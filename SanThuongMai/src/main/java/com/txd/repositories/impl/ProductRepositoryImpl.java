package com.txd.repositories.impl;

import com.txd.pojo.Orderdetail;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.txd.pojo.Product;
import com.txd.repositories.ProductRepository;
import com.txd.utils.GlobalVariables;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tran1
 */
@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Product> getProducts(Map<String, String> params) {
        int PAGE_SIZE = GlobalVariables.PAGE_SIZE;
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Product> cQ = b.createQuery(Product.class);
        Root root = cQ.from(Product.class);
        cQ.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                kw = kw.trim();
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String fromPrice = params.get("fromPrice");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("price"),
                        fromPrice));
            }

            String toPrice = params.get("toPrice");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("price"),
                        toPrice));
            }

            String cateId = params.get("categoryId");
            if (cateId != null && !cateId.isEmpty()) {
                predicates.add(b.equal(root.get("categoryId").as(Integer.class),
                        cateId));
            }
            String shopId = params.get("shopId");
            if (shopId != null && !shopId.isEmpty()) {
                predicates.add(b.equal(root.get("shopId").as(Integer.class),
                        shopId));
            }
            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(b.equal(root.get("isActive"), Boolean.parseBoolean(isActive)));
            }
            cQ.where(predicates.toArray(Predicate[]::new));
        }

// Sắp xếp
        String orderBy = params.getOrDefault("orderBy", "desc");
        String sortBy = params.getOrDefault("sortBy", "id");

        if (sortBy.equalsIgnoreCase("name")) {
            if (orderBy.equalsIgnoreCase("asc")) {
                cQ.orderBy(b.asc(root.get("name")));
            } else {
                cQ.orderBy(b.desc(root.get("name")));
            }
        } else if (sortBy.equalsIgnoreCase("price")) {
            if (orderBy.equalsIgnoreCase("asc")) {
                cQ.orderBy(b.asc(root.get("price")));
            } else {
                cQ.orderBy(b.desc(root.get("price")));
            }
        } else {
            // Mặc định sắp xếp giảm theo id
            if (orderBy.equalsIgnoreCase("asc")) {
                cQ.orderBy(b.asc(root.get("id")));
            } else {
                cQ.orderBy(b.desc(root.get("id")));
            }
        }

        Query query = s.createQuery(cQ);
        if (params != null) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * PAGE_SIZE;
            query.setFirstResult(start);
            query.setMaxResults(PAGE_SIZE);
        }
        // quan he lazy nen phai tu lay neu khong se bij loi lazy
        List<Product> ps = query.getResultList();
        for (Product pro : ps) {
            if (pro != null) {
                Hibernate.initialize(pro.getProductattributeSet());
                Hibernate.initialize(pro.getProductratingSet());

            }
        }
        return ps;
    }

    @Override
    public Integer getSalesQuantity(int productId) {

        try {
            Session session = factory.getObject().getCurrentSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Orderdetail> query = builder.createQuery(Orderdetail.class);
            Root<Orderdetail> root = query.from(Orderdetail.class);
            query.select(root);

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.equal(root.get("productId").get("id"), productId));
            predicates.add(builder.equal(root.get("isActive"), true));
            query.where(predicates.toArray(Predicate[]::new));

            List<Orderdetail> ods = session.createQuery(query).getResultList();
            int sum = 0;
            for (Orderdetail od : ods) {
                sum += od.getQuantity();
            }
            return sum;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Product saveOrUpdate(Product p) {
        Session s = this.factory.getObject().getCurrentSession();
        if (p.getId() == null) {
            s.persist(p);
        } else {
            s.merge(p);
        }
        s.flush();
//        s.refresh(p);
        return p;
    }

    @Override
    public Product getProductById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Product product = s.get(Product.class, id);
        if (product != null) {
            Hibernate.initialize(product.getProductattributeSet());
            Hibernate.initialize(product.getProductratingSet());

        }
        return product;

    }

    // @Override
    // public Long countProducts() {
    //     Session s = this.factory.getObject().getCurrentSession();
    //     CriteriaBuilder b = s.getCriteriaBuilder();
    //     CriteriaQuery<Long> cQ = b.createQuery(Long.class);
    //     Root<Product> root = cQ.from(Product.class);
    //     cQ.select(b.count(root));
    //     Query query = s.createQuery(cQ);
    //     return (Long) query.getSingleResult();
    // }
    @Override
    public Long countProducts(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cQ = b.createQuery(Long.class);
        Root<Product> root = cQ.from(Product.class);
        cQ.select(b.count(root));

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String fromPrice = params.get("fromPrice");
            if (fromPrice != null && !fromPrice.isEmpty()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("price"),
                        Double.parseDouble(fromPrice)));
            }

            String toPrice = params.get("toPrice");
            if (toPrice != null && !toPrice.isEmpty()) {
                predicates.add(b.lessThanOrEqualTo(root.get("price"),
                        Double.parseDouble(toPrice)));
            }

            String cateId = params.get("categoryId");
            if (cateId != null && !cateId.isEmpty()) {
                predicates.add(b.equal(root.get("categoryId").as(Integer.class),
                        Integer.parseInt(cateId)));
            }
            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(b.equal(root.get("isActive"), Boolean.parseBoolean(isActive)));
            }
            String shopId = params.get("shopId");
            if (shopId != null && !shopId.isEmpty()) {
                predicates.add(b.equal(root.get("shopId").as(Integer.class),
                        shopId));
            }
            cQ.where(predicates.toArray(Predicate[]::new));
        }

        Query query = s.createQuery(cQ);
        return (Long) query.getSingleResult();
    }

    @Override
    public void deleteProduct(int id) {
        Session session = factory.getObject().getCurrentSession();
        Product product = session.get(Product.class, id);
        if (product != null) {
            session.remove(product);
        }
    }
}
