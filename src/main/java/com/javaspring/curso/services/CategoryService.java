package com.javaspring.curso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaspring.curso.entities.Category;
import com.javaspring.curso.repositories.CategoryRepo;

@Service
public class CategoryService {
 
  @Autowired
  private CategoryRepo repository;

  public List<Category> findAll() {
    return repository.findAll();
  }

  public Category findById(Long id) {
   Optional<Category> obj =  repository.findById(id);

   return obj.get();
  }
}
