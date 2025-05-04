/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.txd.services.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.txd.pojo.Attribute;
import com.txd.pojo.Category;
import com.txd.pojo.Product;
import com.txd.pojo.Productattribute;
import com.txd.pojo.Shop;
import com.txd.repositories.ProductRepository;
import com.txd.repositories.ProductattributeRepository;
import com.txd.services.AttributeService;
import com.txd.services.ProductService;
import com.txd.services.ShopService;

/**
 *
 * @author tran1
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository proRepo;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductattributeRepository productattributeRepository;
    private static final Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());

    @Override
    public List<Product> getProducts(Map<String, String> params) {
        return proRepo.getProducts(params);
    }

    @Override
    public Product saveOrUpdate(Product p) {
        if (!p.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(p.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                p.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(ProductServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (p.getId() == null) {
            p.setCreateAt(new Date());
            p.setUpdateAt(new Date());
            p.setIsActive(true); // Default for new products
        } else {
            p.setUpdateAt(new Date());
            p.setCreateAt(proRepo.getProductById(p.getId()).getCreateAt());
        }
        return proRepo.saveOrUpdate(p);
    }

    @Override
    public Product getProductById(int id) {
        Product product = proRepo.getProductById(id);
        if (product != null) {
            product.getProductattributeSet();
        }
        return product;
    }

    @Override
    public Long countProducts(Map<String, String> params) {
        return proRepo.countProducts(params);
    }

    @Override
    public void deleteProduct(int id) {
        proRepo.deleteProduct(id);
    }

    @Override
    public Product addProduct(Map<String, Object> params, int sellerId, MultipartFile image) {
        Product product = new Product();
        product.setName((String) params.get("name"));
        product.setDescription((String) params.get("description"));
        product.setPrice(BigDecimal.valueOf(Double.valueOf((String) params.get("price"))));
        Shop shop = shopService.getShopBySellerId(sellerId);
        product.setShopId(shop);
        product.setCategoryId(new Category((int) params.get("categoryId")));
        //chinh image
        if (!image.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                product.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(ProductServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //luu product xong roi luu product attribute
        Product savedProduct = proRepo.saveOrUpdate(product);
        
        Set<Productattribute> attributes = new HashSet<>();
        List<Map<String, Object>> attributeList = (List<Map<String, Object>>) params.get("attributes");
        logger.info("Processing " + attributeList.size() + " attributes for product: " + product.getName());
        for (Map<String, Object> attr : attributeList) {
            Productattribute pa = new Productattribute();
            pa.setProductId(savedProduct);
            Attribute attribute = attributeService.getAttributeById((Integer) attr.get("attributeId"));
            pa.setAttributeId(attribute);
            pa.setValue((String) attr.get("value"));
            //luu xong moi add
            Productattribute savedPa = productattributeRepository.save(pa);
            attributes.add(savedPa);
        }
        product.setProductattributeSet(attributes);

        return proRepo.saveOrUpdate(product);
    }

    @Override
    public Product updateProduct(Map<String, Object> params, int productId, MultipartFile image) {
        Product product = proRepo.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }

        product.setName((String) params.get("name"));
        product.setDescription((String) params.get("description"));
        product.setPrice(BigDecimal.valueOf(Double.valueOf((String) params.get("price"))));
        product.setCategoryId(new Category((int) params.get("categoryId")));
        
        //chinh image
        if (!image.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                product.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(ProductServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
        //  xoa cac attribute 
        productattributeRepository.deleteByProductId(productId);
        product.getProductattributeSet().clear();
        Set<Productattribute> attributes = new HashSet<>();
        List<Map<String, Object>> attributeList = (List<Map<String, Object>>) params.get("attributes");
        for (Map<String, Object> attr : attributeList) {
            Productattribute pa = new Productattribute();
            pa.setProductId(product);
            Attribute attribute = attributeService.getAttributeById((Integer) attr.get("attributeId"));
            pa.setAttributeId(attribute);
            pa.setValue((String) attr.get("value"));
            pa.setCreateAt(new Date());
            pa.setUpdateAt(new Date());
            //luu cac attribute xong moi them lai vao product
            Productattribute savedPa = productattributeRepository.save(pa);
            attributes.add(savedPa);
        }
        product.setProductattributeSet(attributes);

        return proRepo.saveOrUpdate(product);
    }
}
