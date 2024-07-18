package com.javaspring.curso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaspring.curso.entities.Order;
import com.javaspring.curso.repositories.OrderRepo;

@Service
public class OrderService {
 
  @Autowired
  private OrderRepo repository;

  public List<Order> findAll() {
    return repository.findAll();
  }

  public Order findById(Long id) {
   Optional<Order> obj =  repository.findById(id);

   return obj.get();
  }
}
