package com.barca.taskmanager.repositorites;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.barca.taskmanager.configs.MongoConfig;
import com.barca.taskmanager.dtos.TaskDto;
import com.barca.taskmanager.models.Task;

@DataMongoTest()
@Import({ MongoConfig.class })
// @ImportAutoConfiguration
@Transactional
class TaskRepositorySliceTest {

  @Autowired
  private TaskRepository taskRepository;

  @BeforeEach
  void setup() {
    taskRepository.deleteAll();
  }

  @Test
  void findAllByUserId_should_find_2_documents() {

    taskRepository.save(new Task(null, "content example 1", null, "1"));
    taskRepository.save(new Task(null, "content example 2", null, "1"));
    taskRepository.save(new Task(null, "content example 3", null, "2"));
    Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");

    Page<TaskDto> page = taskRepository.findAllByUserId("1", pageable);

    assertEquals(true, page.getSort().isSorted());
    assertEquals(2, page.getNumberOfElements());

  }

  @Test
  void findAllByUserId_should_find_0_documents() {

    taskRepository.save(new Task(null, "content example 1", null, "1"));
    Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");

    Page<TaskDto> page = taskRepository.findAllByUserId("3", pageable);

    assertEquals(0, page.getNumberOfElements());
  }

  @Test
  void deleteAllByUserId_should_delete_2_documents() {

    taskRepository.save(new Task(null, "content example 1", null, "1"));
    taskRepository.save(new Task(null, "content example 2", null, "1"));
    taskRepository.save(new Task(null, "content example 3", null, "2"));

    taskRepository.deleteAllByUserId("1");

    assertEquals(1L, taskRepository.count());
    // TODO verify document remaining == task userId 2
  }

  @Test
  void deleteAllByUserId_should_delete_0_documents() {

    taskRepository.save(new Task(null, "content example 2", null, "1"));
    taskRepository.save(new Task(null, "content example 3", null, "2"));

    taskRepository.deleteAllByUserId("3");

    assertEquals(2L, taskRepository.count());
  }

}
