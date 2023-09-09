package com.lcwd.store.services.impl;

import com.lcwd.store.dtos.AddItemToCartRequest;
import com.lcwd.store.dtos.CartDto;
import com.lcwd.store.entities.Cart;
import com.lcwd.store.entities.CartItem;
import com.lcwd.store.entities.Product;
import com.lcwd.store.entities.User;
import com.lcwd.store.exceptions.ResourceNotFoundException;
import com.lcwd.store.repositories.CartItemRepository;
import com.lcwd.store.repositories.CartRepository;
import com.lcwd.store.repositories.ProductRepository;
import com.lcwd.store.repositories.UserRepository;
import com.lcwd.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemsToCart(String userId, Map<String,List<AddItemToCartRequest>> addItemToCartRequest) {
        CartDto cartDto=null;
        for (AddItemToCartRequest cart : addItemToCartRequest.get("cart")) {
            cartDto = addItemToCart(userId, cart);
        }
        return cartDto;
    }
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        //fetch the product
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user Found"));
        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException exception) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        //perform cart operation
        // logic if to update cart items if cart already exist
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items =cart.getItems();
        items=items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                 item.setQuantity(request.getQuantity()+item.getQuantity());
                item.setPrice(item.getQuantity() * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
        // when you set orphan true donot remove reference of older list change the existing list
//        cart.setItems(updatedItems);
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder().quantity(request.getQuantity())
                    .product(product)
                    .price(request.getQuantity() * product.getDiscountedPrice())
                    .cart(cart)
                    .build();
            cart.getItems().add(cartItem);
        }
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("cart item not found"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user Found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found for given user"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user Found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found for given user"));
        return modelMapper.map(cart, CartDto.class);

    }


}
