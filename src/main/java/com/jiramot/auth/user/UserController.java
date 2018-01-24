package com.jiramot.auth.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private UserRepository applicationUserRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserController(UserRepository applicationUserRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.applicationUserRepository = applicationUserRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @PostMapping("/signup")
  public void signUp(@RequestBody User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    applicationUserRepository.save(user);
  }
}
