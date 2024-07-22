package com.javaspring.curso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaspring.curso.entities.User;
import com.javaspring.curso.repositories.UserRepo;
import com.javaspring.curso.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

  @Autowired
  private UserRepo repository;

  public List<User> findAll() {
    return repository.findAll();
  }

  public User findById(Long id) {
    Optional<User> obj = repository.findById(id);

    return obj.orElseThrow(() -> new ResourceNotFoundException(id));
  }

  public User insert(User user) {
    return repository.save(user);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  public User update(Long id, User user) {
    User existingUser = repository.getReferenceById(id);
    updateData(existingUser, user);

    return repository.save(existingUser);
  }

  private void updateData(User existingUser, User user) {
    existingUser.setName(user.getName());
    existingUser.setEmail(user.getEmail());
    existingUser.setPhone(user.getPhone());
  }
}
