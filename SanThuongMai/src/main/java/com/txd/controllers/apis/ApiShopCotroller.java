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

import com.txd.dto.ShopDTO;
import com.txd.pojo.Seller;
import com.txd.pojo.Shop;
import com.txd.pojo.User;
import com.txd.services.ShopService;
import com.txd.services.UserService;
import com.txd.utils.AuthHelper;
import com.txd.utils.JwtUtils;

/**
 *
 * @author tran1
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiShopCotroller {

    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userDetailsService;
    @Autowired
    private AuthHelper authHelper;

    @GetMapping("/shops")
    public ResponseEntity<List<ShopDTO>> list(@RequestParam Map<String, String> params) {
        List<Shop> shops = this.shopService.getShops(params);
        List<ShopDTO> shopDTOs = shops.stream().map(ShopDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(shopDTOs, HttpStatus.OK);
    }

    @GetMapping("/shops/user/{userId}")
    public ResponseEntity<ShopDTO> single(@PathVariable(value = "userId") int sellerId) {
        Shop shop = this.shopService.getShopBySellerId(sellerId);
        ShopDTO shopDTO = new ShopDTO(shop);
        if (shopDTO.getId() == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(shopDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/shops")
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody Map<String, String> params,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> authResult = authHelper.getUsernameFromToken(authHeader, "Seller");
        if (!"success".equals(authResult.get("status"))) {
            return new ResponseEntity<>(authResult, HttpStatus.UNAUTHORIZED);
        }
        try {
            String username = (String) authResult.get("username");
            User user = userDetailsService.getUserByUsername(username);
            if (user != null) {
                //kiem tra da approve chua
                Seller seller = user.getSeller();
                if (seller.getStatus().equals(Seller.SellerStatusEnum.PENDING) || seller.getStatus().equals(Seller.SellerStatusEnum.REJECT)) {
                    response.put("status", "fail");
                    response.put("error", "người dùng chưa được duyệt hoặc đã bị từ chối");
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                Shop shop = shopService.addShop(params, user.getId());
                ShopDTO shopDTO = new ShopDTO(shop);

                response.put("status", "success");
                response.put("shop", shopDTO);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("status", "fail");
                response.put("error", "Không rõ user");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Lỗi khi tạo shop: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/shops/{shopId}")
    public ResponseEntity<Map<String, Object>> update(
            @RequestBody Map<String, String> params,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable(value = "shopId") int id) {

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> authResult = authHelper.getUsernameFromToken(authHeader, "Seller");
        if (!"success".equals(authResult.get("status"))) {
            return new ResponseEntity<>(authResult, HttpStatus.UNAUTHORIZED);
        }
        try {
            String username = (String) authResult.get("username");
            User user = userDetailsService.getUserByUsername(username);

            if (user != null) {

                //kiem tra co nguoi ban co du quyen tao san pham khong
                Seller seller = user.getSeller();
                if (seller.getStatus().equals(Seller.SellerStatusEnum.PENDING) || seller.getStatus().equals(Seller.SellerStatusEnum.REJECT)) {
                    response.put("status", "fail");
                    response.put("error", "người dùng chưa được duyệt hoặc đã bị từ chối");
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                Shop shop = shopService.getShopById(id);
                shop.setName(params.get("name"));
                shop.setIsActive(Boolean.valueOf(params.get("isActive")));
                shopService.saveOrUpdate(shop);

                response.put("status", "success");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("status", "fail");
                response.put("error", "Không rõ user");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            response.put("status", "fail");
            response.put("error", "Lỗi khi tạo shop: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/shops/{shopId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroyShop(@PathVariable(value = "shopId") int id) {
        this.shopService.deleteShop(id);
    }
}
