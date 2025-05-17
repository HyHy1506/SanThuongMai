/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author tran1
 */
@Entity
@Table(name = "seller")
@NamedQueries({
    @NamedQuery(name = "Seller.findAll", query = "SELECT s FROM Seller s"),
    @NamedQuery(name = "Seller.findByUserId", query = "SELECT s FROM Seller s WHERE s.userId = :userId"),
    @NamedQuery(name = "Seller.findByStatus", query = "SELECT s FROM Seller s WHERE s.status = :status")})
public class Seller implements Serializable {

    public enum SellerStatusEnum {
        PENDING, APPROVED, REJECT
    }
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SellerStatusEnum status = SellerStatusEnum.PENDING;
    @Basic(optional = false)
    @NotNull
    @Column(name = "account_balance")
    private BigDecimal accountBalance;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "sellerId")
    private Shop shop;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sellerId")
    private Set<Sellerrating> sellerratingSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sellerId")
    private Set<Sellerreview> sellerreviewSet;

    
    @PrePersist
    protected void onCreate() {
//        this.accountBalance=BigDecimal.valueOf(0);
    }
    public Seller() {
    }

    public Seller(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public SellerStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SellerStatusEnum status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seller)) {
            return false;
        }
        Seller other = (Seller) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.txd.pojo.Seller[ userId=" + userId + " ]";
    }

    /**
     * @return the accountBalance
     */
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    /**
     * @param accountBalance the accountBalance to set
     */
    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

}
