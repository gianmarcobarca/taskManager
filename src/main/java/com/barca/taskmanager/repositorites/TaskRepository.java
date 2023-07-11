package com.barca.taskmanager.repositorites;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.barca.taskmanager.dtos.TaskDto;
import com.barca.taskmanager.models.Task;

public interface TaskRepository extends MongoRepository<Task, String> {

  Page<TaskDto> findAllByUserId(String userId, Pageable pageable);

  void deleteAllByUserId(String userId);
}
