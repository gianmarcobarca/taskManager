package com.barca.taskmanager.repositorites;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest()
public class TaskRepositorySliceTest {

  @Autowired
  private TaskRepository taskRepository;

  @Test
  void testDeleteAllByUserId() {

  }

  @Test
  void testFindAllByUserId() {

  }
}
