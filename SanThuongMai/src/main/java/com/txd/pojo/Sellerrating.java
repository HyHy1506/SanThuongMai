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
@Table(name = "sellerrating")
@NamedQueries({
    @NamedQuery(name = "Sellerrating.findAll", query = "SELECT s FROM Sellerrating s"),
    @NamedQuery(name = "Sellerrating.findById", query = "SELECT s FROM Sellerrating s WHERE s.id = :id"),
    @NamedQuery(name = "Sellerrating.findByRate", query = "SELECT s FROM Sellerrating s WHERE s.rate = :rate"),
    @NamedQuery(name = "Sellerrating.findByCreateAt", query = "SELECT s FROM Sellerrating s WHERE s.createAt = :createAt"),
    @NamedQuery(name = "Sellerrating.findByIsActive", query = "SELECT s FROM Sellerrating s WHERE s.isActive = :isActive"),
    @NamedQuery(name = "Sellerrating.findByUpdateAt", query = "SELECT s FROM Sellerrating s WHERE s.updateAt = :updateAt")})
public class Sellerrating implements Serializable {

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
    @JoinColumn(name = "seller_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private Seller sellerId;

    public Sellerrating() {
    }

    public Sellerrating(Integer id) {
        this.id = id;
    }

    public Sellerrating(Integer id, RatingEnum rate) {
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

    public Seller getSellerId() {
        return sellerId;
    }

    public void setSellerId(Seller sellerId) {
        this.sellerId = sellerId;
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
        if (!(object instanceof Sellerrating)) {
            return false;
        }
        Sellerrating other = (Sellerrating) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.txd.pojo.Sellerrating[ id=" + id + " ]";
    }
    
}
