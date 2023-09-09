package com.lcwd.store.repositories;

import com.lcwd.store.entities.Order;
import com.lcwd.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
}
