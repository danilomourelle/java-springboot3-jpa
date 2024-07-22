package com.javaspring.curso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaspring.curso.entities.User;
import com.javaspring.curso.repositories.UserRepo;

@Service
public class UserService {
 
  @Autowired
  private UserRepo repository;

  public List<User> findAll() {
    return repository.findAll();
  }

  public User findById(Long id) {
   Optional<User> obj =  repository.findById(id);

   return obj.get();
  }

  public User insert(User user) {
    return repository.save(user);
  }

  public void delete(Long userId) {
    repository.deleteById(userId);
  }
}
