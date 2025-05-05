/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers.apis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.txd.dto.CommentDTO;
import com.txd.pojo.Comment;
import com.txd.pojo.Customer;
import com.txd.pojo.Product;
import com.txd.pojo.User;
import com.txd.services.CommentService;
import com.txd.services.UserService;
import com.txd.utils.AuthHelper;

/**
 *
 * @author tran1
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userDetailsService;
    @Autowired
    private AuthHelper authHelper;

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByProduct(@RequestParam("productId") int productId) {
        List<Comment> comments = commentService.getCommentsByProductId(productId);
        List<CommentDTO> commentDTOs = comments.stream().map(CommentDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<Map<String, Object>> createComment(
            @RequestBody CommentDTO commentDTO,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> authResult = authHelper.getUsernameFromToken(authHeader, "Customer");
        if (!"success".equals(authResult.get("status"))) {
            return new ResponseEntity<>(authResult, HttpStatus.UNAUTHORIZED);
        }
        try {
            String username = (String) authResult.get("username");
            User user = userDetailsService.getUserByUsername(username);

            Comment comment = new Comment();
            comment.setContent(commentDTO.getContent());
            comment.setProductId(new Product(commentDTO.getProductId()));
            comment.setCustomerId(new Customer(user.getId()));

            Comment savedComment = commentService.addComment(comment);
            response.put("status", "success");
            response.put("comment", new CommentDTO(savedComment));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Lỗi tạo comment: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable("commentId") int commentId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> authResult = authHelper.getUsernameFromToken(authHeader, "Customer");
        if (!"success".equals(authResult.get("status"))) {
            return new ResponseEntity<>(authResult, HttpStatus.UNAUTHORIZED);
        }
        try {
            String username = (String) authResult.get("username");
            User user = userDetailsService.getUserByUsername(username);
            Comment comment = commentService.getCommentById(commentId);
            if (comment == null) {
                response.put("status", "fail");
                response.put("error", "Không tìm thấy bình luận");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (!user.getId().equals(comment.getCustomerId().getUserId())) {
                response.put("status", "fail");
                response.put("error", "Bạn không được phân quyền để xóa comment này");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }

            commentService.deleteComment(commentId);
            response.put("status", "success");
            response.put("message", "Xóa comment thành công");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Lỗi xóa comment: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
