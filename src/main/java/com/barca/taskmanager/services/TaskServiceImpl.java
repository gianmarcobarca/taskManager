package com.barca.taskmanager.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barca.taskmanager.dtos.TaskCreationDto;
import com.barca.taskmanager.dtos.TaskDto;
import com.barca.taskmanager.models.Task;
import com.barca.taskmanager.repositorites.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service("taskService")
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  @Override
  public void createTask(String userId, TaskCreationDto dto) {
    taskRepository.save(Task.of(dto.content(), userId));
  }

  @Override
  public Page<TaskDto> getUserTasks(String userId, Pageable pageable) {
    return taskRepository.findAllByUserId(userId, pageable);
  }

  @Override
  public void deleteTask(String userId, String taskId) {
    Optional<Task> result = taskRepository.findById(taskId);
    Task task = result.orElseThrow();

    if (!(userId.equals(task.getUserId()))) {
      throw new DataIntegrityViolationException("Invalid user ID");
    }
    taskRepository.deleteById(taskId);
  }

  @Override
  public void deleteUserTasks(String userId) {
    taskRepository.deleteAllByUserId(userId);
  }

}
