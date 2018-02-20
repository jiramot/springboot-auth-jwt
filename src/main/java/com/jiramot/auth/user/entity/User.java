package com.jiramot.auth.user.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Entity(name = "User")
@Table(name = "user")
public class User {

  private long id;
  private String username;
  private String password;
  private Set<Role> roles;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(unique = true, nullable = false)
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
  @Column(nullable = false)
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    String result = String.format("User [id=%d, name=%s]%n", id, username);
    if (roles != null) {
      for (Role role : roles) {
        result += String.format("Role [id=%d, name=%s]%n", role.getId(), role.getName());
//        if(role.getPermissions() != null){
//          for(Permission permission: role.getPermissions()){
//            result += String.format("Permission [id=%d, name=%s]%n", permission.getId(), permission.getName());
//          }
//        }
      }
    }
    return result;
  }
}
