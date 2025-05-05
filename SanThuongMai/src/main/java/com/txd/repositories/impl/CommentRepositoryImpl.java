/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories.impl;

import com.txd.pojo.Comment;
import com.txd.repositories.CommentRepository;
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

@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Comment> getCommentsByProductId(int productId) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Comment> query = builder.createQuery(Comment.class);
        Root<Comment> root = query.from(Comment.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("productId").get("id"), productId));
        predicates.add(builder.equal(root.get("isActive"), true));
        query.where(predicates.toArray(Predicate[]::new));

        query.orderBy(builder.desc(root.get("createAt")));

        return session.createQuery(query).getResultList();
    }

    @Override
    public Comment addComment(Comment comment) {
        Session session = factory.getObject().getCurrentSession();
        session.persist(comment);
        session.refresh(comment);
        return comment;
    }

    @Override
    public void deleteComment(int id) {
        Session session = factory.getObject().getCurrentSession();
        Comment comment = session.get(Comment.class, id);
        if (comment != null) {
            session.remove(comment);
        }
    }

    @Override
    public Comment getCommentById(int id) {
        Session session = factory.getObject().getCurrentSession();
        return session.get(Comment.class, id);
    }
}
