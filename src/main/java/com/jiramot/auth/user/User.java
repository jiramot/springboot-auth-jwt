package com.jiramot.auth.user;


import javax.persistence.*;

@Entity(name = "User")
@Table(name = "application_user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String username;
  private String password;

  public long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

//  @Transient
//  public List<String> getRoles() {
//    return Arrays.asList("READ");
//  }
}
