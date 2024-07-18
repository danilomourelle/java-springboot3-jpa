package com.javaspring.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaspring.curso.entities.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {
} 
