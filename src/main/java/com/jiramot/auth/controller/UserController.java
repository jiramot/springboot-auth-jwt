package com.jiramot.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @GetMapping
  public @ResponseBody
  String getUser() {
    return "{\"users\":[{\"firstname\":\"Richard\", \"lastname\":\"Feynman\"}," +
        "{\"firstname\":\"Marie\",\"lastname\":\"Curie\"}]}";
  }
}
