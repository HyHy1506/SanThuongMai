/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.formatters;

import com.txd.pojo.Seller;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author tran1
 */
public class SellerFormatter implements Formatter<Seller>{

    @Override
    public String print(Seller object, Locale locale) {
        return String.valueOf(object.getUserId());
    }

    @Override
    public Seller parse(String text, Locale locale) throws ParseException {
        Seller s= new Seller();
        s.setUserId(Integer.parseInt(text));
        return s;
        
    }
    
}