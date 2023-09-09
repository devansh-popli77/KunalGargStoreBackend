package com.lcwd.store.services.impl;

import com.lcwd.store.dtos.CategoryDto;
import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.entities.Category;
import com.lcwd.store.entities.Product;
import com.lcwd.store.exceptions.ResourceNotFoundException;
import com.lcwd.store.helper.HelperUtils;
import com.lcwd.store.repositories.CategoryRepository;
import com.lcwd.store.services.CategoryService;
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
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Value("${category.cover.image.path}")
    private String imageUploadPath;

    @Override
    public CategoryDto createcategory(CategoryDto categoryDto) {
        categoryDto.setCategoryId(UUID.randomUUID().toString());
        Category category = categoryRepository.save(modelMapper.map(categoryDto, Category.class));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updatecategory(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        String fullPath=imageUploadPath+category.getCoverImage();
        Path path= Paths.get(fullPath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(category.getProducts()!=null && !CollectionUtils.isEmpty(category.getProducts()))
        {
            for(Product product: category.getProducts())
            {
                product.getCategories().remove(category);
            }
            category.getProducts().clear();
            categoryRepository.save(category);
        }
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> response = HelperUtils.getPageableResponse(categoriesPage, CategoryDto.class);
        return response;
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return modelMapper.map(category, CategoryDto.class);
    }
}
