/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author tran1
 */
@Entity
@Table(name = "payment")
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p"),
    @NamedQuery(name = "Payment.findById", query = "SELECT p FROM Payment p WHERE p.id = :id"),
    @NamedQuery(name = "Payment.findByPrice", query = "SELECT p FROM Payment p WHERE p.price = :price"),
    @NamedQuery(name = "Payment.findByPaymentMethod", query = "SELECT p FROM Payment p WHERE p.paymentMethod = :paymentMethod"),
    @NamedQuery(name = "Payment.findByIsPay", query = "SELECT p FROM Payment p WHERE p.isPay = :isPay"),
    @NamedQuery(name = "Payment.findByUpdateAt", query = "SELECT p FROM Payment p WHERE p.updateAt = :updateAt"),
    @NamedQuery(name = "Payment.findByIsActive", query = "SELECT p FROM Payment p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "Payment.findByCreateAt", query = "SELECT p FROM Payment p WHERE p.createAt = :createAt")})
public class Payment implements Serializable {

    public enum PaymentMethodEnum {
        COD, Paypal, Stripe, ZaloPay, Momo
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethodEnum paymentMethod;
    @Column(name = "is_pay")
    private Boolean isPay;
    @Column(name = "is_pay_for_seller")
    private Boolean isPayForSeller;
    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentId", fetch = FetchType.EAGER)
//    private Set<Paymentdetail> paymentdetailSet;
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private Customer customerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentId")
    private Set<Orderdetail> orderdetailSet;

    public Payment() {
    }

    public Payment(Integer id) {
        this.id = id;
    }

    public Payment(Integer id, BigDecimal price, PaymentMethodEnum paymentMethod) {
        this.id = id;
        this.price = price;
        this.paymentMethod = paymentMethod;
    }

    @PrePersist
    protected void onCreate() {
        this.createAt = new Date();
        this.updateAt = new Date();
//        isPayForSeller=false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PaymentMethodEnum getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean isPay) {
        this.isPay = isPay;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

//    public Set<Paymentdetail> getPaymentdetailSet() {
//        return paymentdetailSet;
//    }
//
//    public void setPaymentdetailSet(Set<Paymentdetail> paymentdetailSet) {
//        this.paymentdetailSet = paymentdetailSet;
//    }
    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
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
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.txd.pojo.Payment[ id=" + id + " ]";
    }

    /**
     * @return the orderdetailSet
     */
    public Set<Orderdetail> getOrderdetailSet() {
        return orderdetailSet;
    }

    /**
     * @param orderdetailSet the orderdetailSet to set
     */
    public void setOrderdetailSet(Set<Orderdetail> orderdetailSet) {
        this.orderdetailSet = orderdetailSet;
    }

    /**
     * @return the isPayForSeller
     */
    public Boolean getIsPayForSeller() {
        return isPayForSeller;
    }

    /**
     * @param isPayForSeller the isPayForSeller to set
     */
    public void setIsPayForSeller(Boolean isPayForSeller) {
        this.isPayForSeller = isPayForSeller;
    }

}
