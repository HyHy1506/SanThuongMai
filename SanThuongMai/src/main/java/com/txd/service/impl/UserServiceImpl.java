/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.txd.pojo.User;
import com.txd.repository.UserRepository;
import com.txd.service.UserService;

/**
 *
 * @author tran1
 */
@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getUserRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public User getUserByUsername(String username) {
       return userRepo.getUserByUsername(username);
    }

    @Override
    public User register(Map<String, String> params, MultipartFile avatar) {
         User u = new User();
         u.setNickname(params.get("nickname").trim());
         u.setUsername(params.get("username").trim());
         u.setEmail(params.get("email").trim());
         u.setPassword(this.passswordEncoder.encode(params.get("password").trim()));
         u.setUserRole("Customer");
         if (!avatar.isEmpty()) {
             try {
                 Map res = cloudinary.uploader().upload(avatar.getBytes(),
                         ObjectUtils.asMap("resource_type", "auto"));
                 u.setAvatar(res.get("secure_url").toString());
             } catch (IOException ex) {
                 Logger.getLogger(ProductServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         
         return this.userRepo.register(u);
    }

}
