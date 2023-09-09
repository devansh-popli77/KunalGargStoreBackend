package com.lcwd.store.services;

import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.UserDto;
import com.lcwd.store.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user,String userId);
    UserDto getUserById(String userId);
    UserDto getUserByEmail(String email);
    void deleteUser(String userId) ;
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
    List<UserDto> searchUser(String keyword);
    Optional<User> getUserByEmailForGoogleAuth(String email);
}
