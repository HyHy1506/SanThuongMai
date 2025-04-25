/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.configs;


 import com.txd.formatters.CategoryFormatter;
 import org.springframework.context.annotation.ComponentScan;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.format.FormatterRegistry;
 import org.springframework.transaction.annotation.EnableTransactionManagement;
 import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
 import org.springframework.web.servlet.config.annotation.EnableWebMvc;
 import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
 /**
  *
  * @author admin
  */
 @Configuration
 @EnableWebMvc
 @EnableTransactionManagement
 @ComponentScan(basePackages = {
     "com.txd.controllers",
     "com.txd.repository",
     "com.txd.service",
     "com.txd.utils"
 })
 public class WebAppContextConfigs implements WebMvcConfigurer {
 
     @Override
     public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
         configurer.enable();
     }
 
     @Override
     public void addFormatters(FormatterRegistry registry) {
         registry.addFormatter(new CategoryFormatter());
     }
     
     
 }