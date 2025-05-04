/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.dto;

import com.txd.pojo.Attribute;

/**
 *
 * @author tran1
 */
public class AttributeDTO {
    private Integer id;
    private String name;
    private Boolean isActive;
    
    public AttributeDTO() {
    }
    
    public AttributeDTO(Attribute attribute) {
        this.id = attribute.getId();
        this.name = attribute.getName();
        this.isActive = attribute.getIsActive();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
