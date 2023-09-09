package com.lcwd.store.dtos;

import com.lcwd.store.validations.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;
    @Size(min = 3, max = 20, message = "Invalid Name !!")
    private String name;
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid User Email !!")
    private String email;
    @NotBlank(message = "Password is required!!")
    private String password;
    @Size(min = 4, max = 6, message = "Invalid Gender!!")
    private String gender;
    @NotBlank(message = "Write something about yourself!!")
    private String about;
    //custom validator
//    @ImageNameValid
    private String imageName;
    private Set<RoleDto> roles=new HashSet<>();
}
