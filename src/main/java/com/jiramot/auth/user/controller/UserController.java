package com.jiramot.auth.user.controller;

import com.jiramot.auth.user.entity.Role;
import com.jiramot.auth.user.entity.User;
import com.jiramot.auth.user.model.UserResponseDto;
import com.jiramot.auth.user.repository.RoleRepository;
import com.jiramot.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@RestController
public class UserController {

  private UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private RoleRepository roleRepository;

  @Autowired
  public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.roleRepository = roleRepository;
  }

  @PostMapping("/signup")
  public UserResponseDto signup(@RequestBody User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    Role adminRole = roleRepository.findByName("ADMIN");
    Set<Role> roles = new HashSet<Role>() {{
      add(adminRole);
    }};
    user.setRoles(roles);
    userRepository.save(user);
    return new UserResponseDto(user);
  }
}
