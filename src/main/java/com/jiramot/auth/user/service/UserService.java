package com.jiramot.auth.user.service;

import com.jiramot.auth.user.model.User;

import java.util.Optional;

public interface UserService {
  public Optional<User> findByUserName(String username);
}
