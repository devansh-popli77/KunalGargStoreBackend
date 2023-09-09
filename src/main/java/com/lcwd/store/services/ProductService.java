package com.lcwd.store.services;

import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.ProductDto;
import com.lcwd.store.entities.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto, String productId);

    //delete
    void deleteProduct(String productId);

    //get single
    ProductDto getProduct(String productId);


    PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
    //search product

    ProductDto createWithCategory(ProductDto productDto, String categoryId);

    ProductDto updateProductWithCategory(String productId, List<String> categoryId);

    PageableResponse<ProductDto> searchProducts(String productName, int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDto> getAllOfCategory(int pageNumber, int pageSize, String sortBy, String sortDir, String categoryId);
}
