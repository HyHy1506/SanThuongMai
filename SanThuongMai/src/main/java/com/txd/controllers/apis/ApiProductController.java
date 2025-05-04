/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.controllers.apis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txd.dto.ProductDTO;
import com.txd.dto.ProductDetailDTO;
import com.txd.pojo.Product;
import com.txd.services.ProductService;
import com.txd.services.UserService;
import com.txd.utils.GlobalVariables;
import com.txd.utils.JwtUtils;

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
    @Autowired
    private UserService userDetailsService;

    private ObjectMapper objectMapper = GlobalVariables.getObjectMapper();

    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "productId") int id) {
        this.productService.deleteProduct(id);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> list(@RequestParam Map<String, String> params) {
        List<Product> products = this.productService.getProducts(params);
        List<ProductDTO> proDTOs = products.stream().map(ProductDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(proDTOs, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailDTO> retrieve(@PathVariable(value = "productId") int id) {
        Product product = this.productService.getProductById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ProductDetailDTO dto = new ProductDetailDTO(product);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(path = "/products",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> create(
            @RequestParam("params") String paramsJson,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "image") MultipartFile image) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("status", "fail");
                response.put("error", "Thiếu hoặc sai định dạng Authorization header");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            String username = JwtUtils.validateTokenAndGetUsername(token);
            if (username == null) {
                response.put("status", "fail");
                response.put("error", "Token không hợp lệ hoặc đã hết hạn");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            // doi string json sang map
            Map<String, Object> params = objectMapper.readValue(paramsJson, Map.class);
            Integer userId = userDetailsService.getUserByUsername(username).getId();
            Product product = productService.addProduct(params, userId,image);
            response.put("status", "success");
            response.put("productId", product.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/products/{productId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable(value = "productId") int productId,
            @RequestParam("params") String paramsJson,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "image") MultipartFile image) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.put("status", "fail");
                response.put("error", "Thiếu hoặc sai định dạng Authorization header");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            String username = JwtUtils.validateTokenAndGetUsername(token);
            if (username == null) {
                response.put("status", "fail");
                response.put("error", "oken không hợp lệ hoặc đã hết hạn");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
            // doi string json sang map
            Map<String, Object> params = objectMapper.readValue(paramsJson, Map.class);
            Product product = productService.updateProduct(params, productId, image);
            response.put("status", "success");
            response.put("productId", product.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
