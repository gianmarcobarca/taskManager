package com.barca.taskmanager.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.barca.taskmanager.repositorites.TaskRepository;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceUnitTest {

  @Mock
  private TaskRepository taskRepository;
  @InjectMocks
  private TaskService taskService;

  @Test
  void testGetUserTasks() {
  }

  @Test
  void testDeleteUserTasks() {

  }

}
