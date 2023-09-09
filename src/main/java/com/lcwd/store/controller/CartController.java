package com.lcwd.store.controller;

import com.lcwd.store.dtos.AddItemToCartRequest;
import com.lcwd.store.dtos.ApiResponseMessage;
import com.lcwd.store.dtos.CartDto;
import com.lcwd.store.entities.Cart;
import com.lcwd.store.services.CartService;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest addItemToCartRequest, @PathVariable String userId) {
        CartDto cartDto = cartService.addItemToCart(userId, addItemToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    @PostMapping("/list/{userId}")
    public ResponseEntity<CartDto> addItemsToCart(@RequestBody Map<String,List<AddItemToCartRequest>> addItemToCartRequest, @PathVariable String userId) {
        CartDto cartDto = cartService.addItemsToCart(userId, addItemToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemToCart(@PathVariable String itemId, @PathVariable String userId) {
        cartService.removeItemFromCart(userId, Integer.parseInt(itemId));
        ApiResponseMessage response = ApiResponseMessage.builder().
                message("item removed from cart!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> DeleteAllItemToCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder().
                message("now cart is blank!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getItemToCart(@PathVariable String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
