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

import com.txd.pojo.Admin;
import com.txd.pojo.Customer;
import com.txd.pojo.Seller;
import com.txd.pojo.Staff;
import com.txd.pojo.User;
import com.txd.pojo.UserRoleEnum;
import com.txd.repositories.AdminRepository;
import com.txd.repositories.CustomerRepository;
import com.txd.repositories.SellerRepository;
import com.txd.repositories.StaffRepository;
import com.txd.repositories.UserRepository;
import com.txd.utils.GlobalVariables;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author tran1
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private SellerRepository sellerRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private CustomerRepository customerRepo;

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);

        return (User) q.getSingleResult();
    }

    @Override
    public User register(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(u);
        createEntityByRoleOfUser(u);
        s.refresh(u);
        return u;
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("nickname"), "%" + kw + "%"));
            }

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(builder.equal(root.get("isActive"), Boolean.parseBoolean(isActive)));
            }
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }

        String orderBy = params.get("orderBy");

        if (orderBy == null || orderBy.isEmpty() || orderBy.equalsIgnoreCase("desc")) {
            query.orderBy(builder.desc(root.get("id")));
        } else if (orderBy.equalsIgnoreCase("asc")) {
            query.orderBy(builder.asc(root.get("id")));
        }
        query.orderBy(builder.asc(root.get("id")));

        Query q = session.createQuery(query);

        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = GlobalVariables.PAGE_SIZE;
        q.setFirstResult((page - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }

    @Override
    public long countUsers(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        query.select(builder.count(root));

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("nickname"), "%" + kw + "%"));
            }

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(builder.equal(root.get("isActive"), Boolean.valueOf(isActive)));
            }
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }

        return session.createQuery(query).getSingleResult();
    }

    @Override
    public User getUserById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public User saveOrUpdate(User user) {
        Session session = this.factory.getObject().getCurrentSession();
        if (user.getId() == null) {
            session.persist(user);
            createEntityByRoleOfUser(user);
        } else {
            session.merge(user);
        }

        return user;
    }

    @Override
    public void deleteUser(int id) throws IllegalArgumentException {
        Session session = this.factory.getObject().getCurrentSession();
        User user = session.get(User.class, id);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy id: " + id);
        }

        try {
            deleteEntityByRoleOfUser(user);
            session.remove(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Lỗi : " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("An unexpected error occurred while deleting user: " + e.getMessage(), e);
        }

    }

    private void deleteEntityByRoleOfUser(User user) {
        if (user != null) {
            try {
                UserRoleEnum userRole = UserRoleEnum.valueOf(user.getUserRole());
                switch (userRole) {
                    case Admin:
                        adminRepo.deleteAdmin(user.getId());
                        break;
                    case Customer:
                        customerRepo.deleteCustomer(user.getId());
                        break;
                    case Seller:
                        Seller seller = sellerRepo.getSellerById(user.getId());
                        if (seller != null) {
                            // Kiểm tra xem Seller có cửa hàng không

                            if (sellerRepo.hasRelatedProductsOrShops(user.getId())) {
                                throw new IllegalArgumentException("Không thể xóa seller, hãy xóa theo thứ tự sau , product->shop->seller");

                            }
                            sellerRepo.deleteSeller(user.getId());
                        }
                        break;
                    case Staff:
                        staffRepo.deleteStaff(user.getId());
                        break;
                    default:
                        throw new IllegalArgumentException("Không biết role: " + userRole);
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Xóa thực thể kế thừa user thất bại : "+ e);
            }

        }
    }

    private void createEntityByRoleOfUser(User user) {
        if (user != null) {
            try {
                UserRoleEnum userRole = UserRoleEnum.valueOf(user.getUserRole());
                switch (userRole) {
                    case Admin:
                        adminRepo.saveOrUpdate(new Admin(user.getId()));
                        break;
                    case Customer:
                        customerRepo.saveOrUpdate(new Customer(user.getId()));
                        break;
                    case Seller:
                        Seller newSeller = new Seller(user.getId());
                        newSeller.setStatus(Seller.SellerStatusEnum.PENDING);
                        sellerRepo.saveOrUpdate(newSeller);
                        break;
                    case Staff:
                        staffRepo.saveOrUpdate(new Staff(user.getId()));
                        break;
                    default:
                        throw new IllegalArgumentException("Không biết role: " + userRole);
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Chỉnh sửa thực thể kế thừa user thất bại ", e);
            }

        }
    }
}
