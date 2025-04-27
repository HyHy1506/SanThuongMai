/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.pojo;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author tran1
 */
@Entity
@Table(name = "productrating")
@NamedQueries({
    @NamedQuery(name = "Productrating.findAll", query = "SELECT p FROM Productrating p"),
    @NamedQuery(name = "Productrating.findById", query = "SELECT p FROM Productrating p WHERE p.id = :id"),
    @NamedQuery(name = "Productrating.findByRate", query = "SELECT p FROM Productrating p WHERE p.rate = :rate"),
    @NamedQuery(name = "Productrating.findByCreateAt", query = "SELECT p FROM Productrating p WHERE p.createAt = :createAt"),
    @NamedQuery(name = "Productrating.findByIsActive", query = "SELECT p FROM Productrating p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "Productrating.findByUpdateAt", query = "SELECT p FROM Productrating p WHERE p.updateAt = :updateAt")})
public class Productrating implements Serializable {

   
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rate")
    private RatingEnum rate;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private Customer customerId;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;

    public Productrating() {
    }

    public Productrating(Integer id) {
        this.id = id;
    }

    public Productrating(Integer id, RatingEnum rate) {
        this.id = id;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RatingEnum getRate() {
        return rate;
    }

    public void setRate(RatingEnum rate) {
        this.rate = rate;
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

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productrating)) {
            return false;
        }
        Productrating other = (Productrating) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.txd.pojo.Productrating[ id=" + id + " ]";
    }

}
