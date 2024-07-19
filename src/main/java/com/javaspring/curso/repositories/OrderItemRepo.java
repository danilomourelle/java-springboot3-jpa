package com.javaspring.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaspring.curso.entities.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
} 
