package com.javaspring.curso.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaspring.curso.entities.User;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

  @GetMapping
  public ResponseEntity<User> findAll() {
    User u = new User(1L, "Danilo", "danilo@email.com", "999999999", "pass");

    return ResponseEntity.ok().body(u);
  }
}
