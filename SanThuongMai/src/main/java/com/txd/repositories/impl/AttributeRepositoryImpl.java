/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories.impl;

import com.txd.pojo.Attribute;
import com.txd.repositories.AttributeRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class AttributeRepositoryImpl implements AttributeRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public List<Attribute> getAttributes() {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Attribute> query = builder.createQuery(Attribute.class);
        Root<Attribute> root = query.from(Attribute.class);
        query.select(root);
        
        Query q = session.createQuery(query);
        return q.getResultList();
    }
    
    @Override
    public Attribute getAttributeById(int id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(Attribute.class, id);
    }
}
