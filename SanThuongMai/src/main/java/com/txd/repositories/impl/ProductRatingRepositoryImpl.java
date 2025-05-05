/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories.impl;

import com.txd.pojo.Productrating;
import com.txd.repositories.ProductRatingRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tran1
 */
@Repository
@Transactional
public class ProductRatingRepositoryImpl implements ProductRatingRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Productrating saveOrUpdateRating(Productrating rating) {
        Session session = factory.getObject().getCurrentSession();
        if (rating.getId() == null) {
            session.persist(rating);
        } else {
            session.merge(rating);
        }
        session.refresh(rating);
        return rating;
    }

    @Override
    public Productrating getRatingByCustomerAndProduct(int customerId, int productId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Productrating> query = builder.createQuery(Productrating.class);
        Root<Productrating> root = query.from(Productrating.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("customerId").get("userId"), customerId));
        predicates.add(builder.equal(root.get("productId").get("id"), productId));
        predicates.add(builder.equal(root.get("isActive"), true));
        query.where(predicates.toArray(Predicate[]::new));

        try {
            return session.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
