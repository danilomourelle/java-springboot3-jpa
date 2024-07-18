package com.javaspring.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaspring.curso.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {
} 
