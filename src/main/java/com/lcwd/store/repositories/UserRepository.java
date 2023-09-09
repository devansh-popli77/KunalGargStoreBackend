package com.lcwd.store.repositories;

import com.lcwd.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
 Optional<User> findByEmail(String email);
 List<User> findByNameContaining(String keyword);
}
