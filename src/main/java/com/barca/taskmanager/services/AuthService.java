package com.barca.taskmanager.services;

import org.springframework.security.core.Authentication;

import com.barca.taskmanager.dtos.JwtDto;
import com.barca.taskmanager.models.Task;

public interface AuthService {

  JwtDto createToken(Authentication auth);

  String encodePassword(String password);

  void checkTaskUserId(String userId, Task task);
}
