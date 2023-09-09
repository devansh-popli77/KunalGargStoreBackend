package com.lcwd.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "products")
public class Product {

    @Id
    private String productId;
    private String title;
    @Lob
    @Column(length = 10000)
    private String description;
    private int price;
    private long discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Category> categories=new ArrayList<>();
}
