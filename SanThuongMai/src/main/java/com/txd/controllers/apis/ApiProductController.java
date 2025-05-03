/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers.apis;

import com.txd.dto.ProductDTO;
import com.txd.pojo.Product;
import com.txd.services.ProductService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tran1
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiProductController {

    @Autowired
    private ProductService productService;

    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "productId") int id) {
        this.productService.deleteProduct(id);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> list(@RequestParam Map<String, String> params) {
        List<Product> products= this.productService.getProducts(params);
        List<ProductDTO> proDTOs=products.stream().map(ProductDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(proDTOs, HttpStatus.OK);
    }
     @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> retrieve(@PathVariable(value = "productId") int id) {
        Product product=this.productService.getProductById(id);
        ProductDTO dto=new ProductDTO(product);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
