package com.txd.repository.impl;

import com.txd.pojo.Product;
import com.txd.repository.ProductRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
public class ProductRepositoryImpl implements ProductRepository{
    private static final int PAGE_SIZE = 6;
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public List<Product> getProducts(Map<String, String> params) {
        Session s= factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Product> cQ= b.createQuery(Product.class);
        Root root= cQ.from(Product.class);
        cQ.select(root);
        
        if(params!=null){
            
        }
        Query query= s.createQuery(cQ);
        if(params!=null){
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page-1)* PAGE_SIZE;
            query.setFirstResult(start);
            query.setMaxResults(PAGE_SIZE);
        }
        
        return query.getResultList();
    }

    @Override
    public Product saveOrUpdate(Product p) {
         Session s = this.factory.getObject().getCurrentSession();
              if (p.getId() == null)
                  s.persist(p);
              else
                  s.merge(p);
          
          return p;
    }

    @Override
    public Product getProductById(int id) {
     Session s = this.factory.getObject().getCurrentSession();
     return s.get(Product.class, id);
    
    }
    
}
