package com.txd.pojo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tran1
 */
public enum RatingEnum {
    ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");

    private final String value;

    RatingEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
