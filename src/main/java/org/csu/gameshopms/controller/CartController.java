package org.csu.gameshopms.controller;

import org.csu.gameshopms.entity.CartDTO;
import org.csu.gameshopms.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    /** 添加到购物车 */
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartDTO req) {
        cartService.addToCart(
                req.getUserId(),
                req.getItemId(),
                req.getEditionId(),
                req.getPrice()
        );
        return ResponseEntity.ok("success");
    }
}
