/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import com.txd.pojo.Comment;
import java.util.Date;

/**
 *
 * @author tran1
 */
public class CommentDTO {

    private Integer id;
    private String content;
    private Integer productId;
    private Integer customerId;
    private String customerNickname;
    private String customerAvatar;
    private Date createAt;
    private Boolean isActive;

    public CommentDTO() {
    }

    public CommentDTO(Comment comment) {
        if (comment != null) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.productId = comment.getProductId() != null ? comment.getProductId().getId() : null;
            this.customerId = comment.getCustomerId() != null ? comment.getCustomerId().getUserId() : null;
            this.customerNickname = comment.getCustomerId() != null && comment.getCustomerId().getUserId() != null
                    ? comment.getCustomerId().getUser().getNickname() : null;
            this.createAt = comment.getCreateAt();
            this.isActive = comment.getIsActive();
            this.customerAvatar = comment.getCustomerId().getUser().getAvatar();

        }
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNickname() {
        return customerNickname;
    }

    public void setCustomerNickname(String customerNickname) {
        this.customerNickname = customerNickname;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the customerAvatar
     */
    public String getCustomerAvatar() {
        return customerAvatar;
    }

    /**
     * @param customerAvatar the customerAvatar to set
     */
    public void setCustomerAvatar(String customerAvatar) {
        this.customerAvatar = customerAvatar;
    }
}
