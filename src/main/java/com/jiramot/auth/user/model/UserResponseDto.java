package com.jiramot.auth.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {
  private Long id;



  public UserResponseDto(User user) {
    this.id = user.getId();
  }
}
