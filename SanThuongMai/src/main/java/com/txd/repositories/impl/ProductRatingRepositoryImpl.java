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
import org.hibernate.query.Query;
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
public class ProductRatingRepositoryImpl implements ProductRatingRepository {

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

    @Override
    public List<Productrating> getRatingsByProduct( int productId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Productrating> query = builder.createQuery(Productrating.class);
        Root<Productrating> root = query.from(Productrating.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("productId").get("id"), productId));
        predicates.add(builder.equal(root.get("isActive"), true));
        query.where(predicates.toArray(Predicate[]::new));

        try {
            return session.createQuery(query).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Double getAverageRating(int productId) {
        List<Productrating> prs= getRatingsByProduct(productId);
        int sum = 0;
        for(Productrating pr : prs){
            int rate=Integer.valueOf( pr.getRate());
            sum+= rate;
        }
        
        double result = (double)sum / (double)prs.size();
        return result;
    }

    @Override
    public Long getRatingCount(int productId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Productrating> root = query.from(Productrating.class);
        query.select(builder.count(root));
        query.where(builder.equal(root.get("productId").as(Integer.class), productId));
        return session.createQuery(query).getSingleResult();
    }
}
