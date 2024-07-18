package com.javaspring.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaspring.curso.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
} 
