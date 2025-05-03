/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers.apis;

import com.txd.dto.CategoryDTO;
import com.txd.pojo.Category;
import com.txd.services.CategoryService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tran1
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCategoryController {

    @Autowired
    private CategoryService cateService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> list(@RequestParam Map<String, String> params) {
        List<Category> categories = this.cateService.getCates();
        List<CategoryDTO> categoryDTOs = categories.stream().map(CategoryDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
    }
}
