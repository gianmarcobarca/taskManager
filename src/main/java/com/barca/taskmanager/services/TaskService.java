package com.barca.taskmanager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.barca.taskmanager.dtos.TaskCreationDto;
import com.barca.taskmanager.dtos.TaskDto;

public interface TaskService {

  void createTask(String userId, TaskCreationDto dto);

  Page<TaskDto> getUserTasks(String userId, Pageable pageable);

  void deleteTask(String userId, String taskId);

  void deleteUserTasks(String userId);
}
