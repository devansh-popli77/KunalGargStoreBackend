package com.lcwd.store.controller;

import com.lcwd.store.dtos.*;
import com.lcwd.store.services.CategoryService;
import com.lcwd.store.services.FileService;
import com.lcwd.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    private FileService fileService;

    @Value("${category.cover.image.path}")
    private String imageUploadPath;
    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto CategoryDto) {
        CategoryDto Category = categoryService.createcategory(CategoryDto);
        return new ResponseEntity<>(Category, HttpStatus.CREATED);
    }

    //update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{CategoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto CategoryDto, @PathVariable("CategoryId") String CategoryId) {
        CategoryDto updatedCategoryDto = categoryService.updatecategory(CategoryDto, CategoryId);
        return new ResponseEntity<>(updatedCategoryDto, HttpStatus.OK);
    }

    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{CategoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("CategoryId") String CategoryId) {
        categoryService.deleteCategory(CategoryId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message("Category Deleted Successfully")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    //getall
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategorys(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,

                                                                         @RequestParam(value = "sortBy", defaultValue = "title", required = false)
                                                                         String sortBy,
                                                                         @RequestParam(value = "sortDir", defaultValue = "asc", required = false)
                                                                         String sortDir) {
        return new ResponseEntity<>(categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir), HttpStatus.CREATED);
    }


    //getCategoryById
    @GetMapping("/{CategoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String CategoryId) {
        return new ResponseEntity<>(categoryService.getCategoryById(CategoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{CategoryId}")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable String CategoryId, @RequestParam("categoryImage") MultipartFile image) throws IOException {
        String fileName = fileService.uploadImage(image, imageUploadPath);
        CategoryDto categoryDto = categoryService.getCategoryById(CategoryId);
        categoryDto.setCoverImage(fileName);
        categoryService.updatecategory(categoryDto, CategoryId);
        ImageResponse response = ImageResponse.builder().success(true).status(HttpStatus.OK).imageName(fileName).message("Image Saved Successfully").build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/image/{CategoryId}")
    public void uploadImage(@PathVariable String CategoryId, HttpServletResponse response) throws IOException {
        CategoryDto categoryDto = categoryService.getCategoryById(CategoryId);
        InputStream fileInputStream = fileService.getResouce(imageUploadPath, categoryDto.getCoverImage());
        StreamUtils.copy(fileInputStream, response.getOutputStream());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{CategoryId}/products")
    public ResponseEntity<ProductDto> saveCategoryWithProduct(@PathVariable String CategoryId, @RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.createWithCategory(productDto, CategoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryWithProduct(@RequestBody List<String> CategoryId, @PathVariable String productId) {
        return new ResponseEntity<>(productService.updateProductWithCategory(productId, CategoryId), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductsByCategorys(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,

                                                                                  @RequestParam(value = "sortBy", defaultValue = "title", required = false)
                                                                                  String sortBy,
                                                                                  @RequestParam(value = "sortDir", defaultValue = "asc", required = false)
                                                                                  String sortDir, @PathVariable String categoryId) {
        return new ResponseEntity<>(productService.getAllOfCategory(pageNumber, pageSize, sortBy, sortDir, categoryId), HttpStatus.CREATED);
    }
}
