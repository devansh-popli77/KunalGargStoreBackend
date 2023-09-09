package com.lcwd.store.repositories;

import com.lcwd.store.entities.Cart;
import com.lcwd.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ref.Reference;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {
    Optional<Cart> findByUser(User user);
}
