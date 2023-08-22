package com.barca.taskmanager.services;

import com.barca.taskmanager.dtos.UserCreationDto;

public interface UserService {
  void createUser(UserCreationDto dto);

  void deleteUser(String userId);
}
