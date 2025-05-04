/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import com.txd.services.AttributeService;

import com.txd.pojo.Attribute;
import com.txd.repositories.AttributeRepository;
import com.txd.services.AttributeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Override
    public List<Attribute> getAttributes() {
        return attributeRepository.getAttributes();
    }

    @Override
    public Attribute getAttributeById(int id) {
        return attributeRepository.getAttributeById(id);
    }
}
