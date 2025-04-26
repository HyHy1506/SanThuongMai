/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories.impl;

import com.txd.pojo.Seller;
import com.txd.repositories.SellerRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tran1
 */
@Transactional
@Repository
public class SellerRepositoryImpl implements SellerRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Seller> getSeller(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Seller> cQ = b.createQuery(Seller.class);
        Root root = cQ.from(Seller.class);
        cQ.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(b.equal(root.get("user").get("isActive"), Boolean.valueOf(isActive)));
            }

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                kw = kw.trim();
                predicates.add(b.or(
                    b.like(root.get("user").get("nickname"), "%" + kw + "%"),
                    b.like(root.get("user").get("username"), "%" + kw + "%")
                ));
            }

            cQ.where(predicates.toArray(Predicate[]::new));
        }
        Query query = s.createQuery(cQ);

        return query.getResultList();
    }

    @Override
    public List<Seller> getSellersWithoutShop(Map<String, String> params) {

        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Seller> cQ = b.createQuery(Seller.class);
        Root<Seller> root = cQ.from(Seller.class);

        // Left join with Shop
        Join<Seller, com.txd.pojo.Shop> shopJoin = root.join("shop", JoinType.LEFT);
        cQ.select(root).where(b.isNull(shopJoin.get("id")));

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            
            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(b.equal(root.get("user").get("isActive"), Boolean.valueOf(isActive)));
            }

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                kw = kw.trim();
                predicates.add(b.or(
                    b.like(root.get("user").get("nickname"), "%" + kw + "%"),
                    b.like(root.get("user").get("username"), "%" + kw + "%")
                ));
            }

            
            cQ.where(b.and(b.isNull(shopJoin.get("id")), b.and(predicates.toArray(Predicate[]::new))));
        } 
        Query query = s.createQuery(cQ);

        return query.getResultList();
    }
}
