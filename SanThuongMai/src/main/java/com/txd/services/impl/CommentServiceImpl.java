/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import com.txd.pojo.Comment;
import com.txd.repositories.CommentRepository;
import com.txd.services.CommentService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tran1
 */
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByProductId(int productId) {
        return commentRepository.getCommentsByProductId(productId);
    }

    @Override
    public Comment addComment(Comment comment) {
        comment.setCreateAt(new Date());
        comment.setIsActive(true);
        return commentRepository.addComment(comment);
    }

    @Override
    public void deleteComment(int id) {
        commentRepository.deleteComment(id);
    }

    @Override
    public Comment getCommentById(int id) {
        return commentRepository.getCommentById(id);
    }
}
