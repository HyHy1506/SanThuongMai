/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.repositories;

import com.txd.pojo.Attribute;
import java.util.List;

public interface AttributeRepository {

    List<Attribute> getAttributes();

    Attribute getAttributeById(int id);
}
