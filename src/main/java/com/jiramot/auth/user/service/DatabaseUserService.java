package com.jiramot.auth.user.service;

import com.jiramot.auth.user.model.User;
import com.jiramot.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DatabaseUserService implements UserService {
  private UserRepository repository;

  @Autowired
  public DatabaseUserService(UserRepository userRepository) {
    this.repository = userRepository;
  }

  @Override
  public Optional<User> findByUserName(String username) {
    return Optional.ofNullable(repository.findByUsername(username));
  }
}
