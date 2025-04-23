/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author tran1
 */
@Entity
@Table(name = "customer")
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c"),
    @NamedQuery(name = "Customer.findByUserId", query = "SELECT c FROM Customer c WHERE c.userId = :userId")})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Set<Productrating> productratingSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Set<Sellerrating> sellerratingSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Set<Sellerreview> sellerreviewSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Set<Comment> commentSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Set<Payment> paymentSet;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Set<Orderdetail> orderdetailSet;

    public Customer() {
    }

    public Customer(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Set<Productrating> getProductratingSet() {
        return productratingSet;
    }

    public void setProductratingSet(Set<Productrating> productratingSet) {
        this.productratingSet = productratingSet;
    }

    public Set<Sellerrating> getSellerratingSet() {
        return sellerratingSet;
    }

    public void setSellerratingSet(Set<Sellerrating> sellerratingSet) {
        this.sellerratingSet = sellerratingSet;
    }

    public Set<Sellerreview> getSellerreviewSet() {
        return sellerreviewSet;
    }

    public void setSellerreviewSet(Set<Sellerreview> sellerreviewSet) {
        this.sellerreviewSet = sellerreviewSet;
    }

    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }

    public Set<Payment> getPaymentSet() {
        return paymentSet;
    }

    public void setPaymentSet(Set<Payment> paymentSet) {
        this.paymentSet = paymentSet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Orderdetail> getOrderdetailSet() {
        return orderdetailSet;
    }

    public void setOrderdetailSet(Set<Orderdetail> orderdetailSet) {
        this.orderdetailSet = orderdetailSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.txd.pojo.Customer[ userId=" + userId + " ]";
    }
    
}
