package com.lcwd.store.dtos;

import com.lcwd.store.entities.Cart;
import com.lcwd.store.entities.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private int cartItemId;
    private ProductDto product;
    private int quantity;
    private int price;
}
