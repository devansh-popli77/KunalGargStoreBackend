package com.lcwd.store.services.impl;

import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.ProductDto;
import com.lcwd.store.entities.Category;
import com.lcwd.store.entities.Product;
import com.lcwd.store.exceptions.ResourceNotFoundException;
import com.lcwd.store.helper.HelperUtils;
import com.lcwd.store.repositories.CategoryRepository;
import com.lcwd.store.repositories.ProductRepository;
import com.lcwd.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Value("${product.image.path}")
    private String imageUploadPath;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        productDto.setProductId(UUID.randomUUID().toString());
        productDto.setAddedDate(new Date());
        Product savedProduct = productRepository.save(modelMapper.map(productDto, Product.class));
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found"));
        product.setTitle(productDto.getTitle());
        product.setStock(productDto.isStock());
        product.setLive(productDto.isLive());
        product.setPrice(productDto.getPrice());
        product.setProductImage(productDto.getProductImage());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found"));
        String imageFullPath = imageUploadPath + product.getProductImage();
        Path path = Paths.get(imageFullPath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(product.getCategories()!=null && !CollectionUtils.isEmpty(product.getCategories()))
        {
            for(Category category: product.getCategories())
            {
                category.getProducts().remove(product);
            }
            product.getCategories().clear();
            productRepository.save(product);
        }
        productRepository.delete(product);
    }

    @Override
    public ProductDto getProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found"));

        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        PageableResponse<ProductDto> response = HelperUtils.getPageableResponse(productPage, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> response = HelperUtils.getPageableResponse(productPage, ProductDto.class);
        return response;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        //fetch the category
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
        productDto.setProductId(UUID.randomUUID().toString());
        productDto.setAddedDate(new Date());
        Product product = modelMapper.map(productDto, Product.class);
        product.getCategories().add(category);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProductWithCategory(String productId, List<String> categoryIds) {
        //fetch the category

        Product product=productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found"));
        product.getCategories().removeAll(product.getCategories());
        for (String categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
            if (!product.getCategories().contains(category)) {
                product.getCategories().add(category);
            }
        }
        product=productRepository.save(product);

        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchProducts(String productName, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = productRepository.findByTitleContaining(productName, pageable);
        PageableResponse<ProductDto> response = HelperUtils.getPageableResponse(productPage, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(int pageNumber, int pageSize, String sortBy, String sortDir, String categoryId) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Category category=new Category();
        category.setCategoryId(categoryId);
        Page<Product> productPage = productRepository.findByCategories(category,pageable);
        PageableResponse<ProductDto> response = HelperUtils.getPageableResponse(productPage, ProductDto.class);
        return response;
    }

}
