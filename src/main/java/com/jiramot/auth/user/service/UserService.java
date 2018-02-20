package com.jiramot.auth.user.service;

import com.jiramot.auth.user.entity.User;
import com.jiramot.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> findByUsername(String username){
    return userRepository.findByUsername(username);
  }
}
