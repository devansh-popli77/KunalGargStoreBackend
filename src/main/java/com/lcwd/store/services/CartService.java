package com.lcwd.store.services;

import com.lcwd.store.dtos.AddItemToCartRequest;
import com.lcwd.store.dtos.CartDto;

import java.util.List;
import java.util.Map;

public interface CartService {
    // add items to cart
    // case1: cart for user is not available: we will create the cart and
//    case2: cart available add the items to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    // remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    //    remove all items from cart
    void clearCart(String userId);

    CartDto getCartByUser(String userid);

    CartDto addItemsToCart(String userId, Map<String, List<AddItemToCartRequest>> addItemToCartRequest);
}
