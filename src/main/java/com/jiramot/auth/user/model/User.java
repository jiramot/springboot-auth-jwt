package com.jiramot.auth.user.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity(name = "User")
@Table(name = "user")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Transient
  public List<String> getRoles() {
    return Arrays.asList("READ");
  }
}
