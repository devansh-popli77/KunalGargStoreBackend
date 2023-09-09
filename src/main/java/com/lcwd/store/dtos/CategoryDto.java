package com.lcwd.store.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String categoryId;
    @NotBlank(message = "title must not be blank")
    @Size(min = 4,message = "title must be minimum of 4 characters")
    private String title;
    @NotBlank(message = "description should not be blank")
    private String description;
//    @NotBlank(message = "coverImage should not be blank")
    private String coverImage;

}