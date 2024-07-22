package com.javaspring.curso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.javaspring.curso.entities.User;
import com.javaspring.curso.repositories.UserRepo;
import com.javaspring.curso.services.exceptions.DatabaseException;
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
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException(e.getMessage());
    }
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
