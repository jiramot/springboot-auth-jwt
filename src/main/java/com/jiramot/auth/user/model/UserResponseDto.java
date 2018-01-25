package com.jiramot.auth.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {
  private String uuid;


  public UserResponseDto(User user) {
    this.uuid = user.getUuid();
  }
}
