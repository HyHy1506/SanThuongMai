/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories.impl;

import com.txd.pojo.Productattribute;
import com.txd.repositories.ProductattributeRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ProductattributeRepositoryImpl implements ProductattributeRepository{
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Override
    public Productattribute save(Productattribute productattribute) {
        Session session = factory.getObject().getCurrentSession();
        if (productattribute.getId() == null) {
            session.persist(productattribute);
        } else {
            session.merge(productattribute);
        }
        session.flush();
        return productattribute;
    }
    
    @Override
    public void deleteByProductId(int productId) {
        Session session = factory.getObject().getCurrentSession();
        session.createQuery("DELETE FROM Productattribute pa WHERE pa.productId.id = :productId")
               .setParameter("productId", productId)
               .executeUpdate();
    }
}
