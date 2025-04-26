package com.txd.repositories.impl;

import com.txd.pojo.Seller;
import com.txd.pojo.Shop;
import com.txd.repositories.ShopRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.txd.utils.GlobalVariables;

/**
 * Implementation of ShopRepository for managing Shop entities.
 */
@Repository
@Transactional
public class ShopRepositoryImpl implements ShopRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private GlobalVariables globalVariables;

    @Override
    public List<Shop> getShops(Map<String, String> params) {
        int PAGE_SIZE = globalVariables.PAGE_SIZE;
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Shop> cQ = b.createQuery(Shop.class);
        Root<Shop> root = cQ.from(Shop.class);
        cQ.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                kw = kw.trim();
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(b.equal(root.get("isActive"), Boolean.parseBoolean(isActive)));
            }

            cQ.where(predicates.toArray(Predicate[]::new));
        }

        Query query = s.createQuery(cQ);
        if (params != null) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * PAGE_SIZE;
            query.setFirstResult(start);
            query.setMaxResults(PAGE_SIZE);
        }

        return query.getResultList();
    }

    @Override
    public Shop getShopById(int id) {
        Session s = factory.getObject().getCurrentSession();
        return s.get(Shop.class, id);
    }

    @Override
    public void saveOrUpdate(Shop shop) {
        Session s = factory.getObject().getCurrentSession();
        if (shop.getId() == null) {
            s.persist(shop);
        } else {
            s.merge(shop);
        }
    }

    @Override
    public Long countShops(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cQ = b.createQuery(Long.class);
        Root<Shop> root = cQ.from(Shop.class);
        cQ.select(b.count(root));

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(b.equal(root.get("isActive"), Boolean.valueOf(isActive)));
            }

            cQ.where(predicates.toArray(Predicate[]::new));
        }

        Query query = s.createQuery(cQ);
        return (Long) query.getSingleResult();
    }

    @Override
    public void deleteShop(int id) {
        Session s = factory.getObject().getCurrentSession();
        Shop shop = this.getShopById(id);
        if (shop != null) {

            //xoa quan he sheller voi shop
            Seller seller = shop.getSellerId();
            if (seller != null) {
                seller.setShop(null); 
                s.merge(seller); 
            }
            s.remove(shop);
        }
    }
}
