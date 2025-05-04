/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author tran1
 */

@Component
public class GlobalVariables {
    public static final  int PAGE_SIZE = 6;
    private static ObjectMapper objectMapper=new ObjectMapper();
    public static final ObjectMapper getObjectMapper(){
        return objectMapper;
    } 
}
