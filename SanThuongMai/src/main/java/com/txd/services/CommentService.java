/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services;

import com.txd.pojo.Comment;
import java.util.List;
import java.util.Map;

public interface CommentService {

    List<Comment> getCommentsByProductId(int productId);

    Comment addComment(Comment comment);

    void deleteComment(int id);

    Comment getCommentById(int id);

    Long getCommentCount(int productId);
}
