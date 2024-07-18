package com.javaspring.curso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaspring.curso.entities.Product;
import com.javaspring.curso.repositories.ProductRepo;

@Service
public class ProductService {
 
  @Autowired
  private ProductRepo repository;

  public List<Product> findAll() {
    return repository.findAll();
  }

  public Product findById(Long id) {
   Optional<Product> obj =  repository.findById(id);

   return obj.get();
  }
}
