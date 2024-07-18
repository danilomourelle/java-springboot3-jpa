package com.javaspring.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaspring.curso.entities.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
} 
