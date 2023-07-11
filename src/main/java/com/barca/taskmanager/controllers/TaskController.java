package com.barca.taskmanager.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.barca.taskmanager.annotations.Principal;
import com.barca.taskmanager.dtos.TaskCreationDto;
import com.barca.taskmanager.dtos.TaskDto;
import com.barca.taskmanager.services.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController("taskController")
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @GetMapping
  public Page<TaskDto> getUserTasks(@Principal Jwt jwt,
      @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
    return taskService.getUserTasks(jwt.getSubject(), pageable);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createTask(@Principal Jwt jwt, @RequestBody @Valid TaskCreationDto dto) {
    taskService.createTask(jwt.getSubject(), dto);
  }

  @DeleteMapping("/{taskId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTask(@Principal Jwt jwt, @PathVariable String taskId) {
    taskService.deleteTask(jwt.getSubject(), taskId);
  }

  @DeleteMapping()
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUserTasks(@Principal Jwt jwt) {
    taskService.deleteUserTasks(jwt.getSubject());
  }
}
