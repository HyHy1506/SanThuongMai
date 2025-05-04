/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services;

import com.txd.pojo.Attribute;
import java.util.List;

/**
 *
 * @author tran1
 */
public interface AttributeService {

    List<Attribute> getAttributes();

    Attribute getAttributeById(int id);
}
