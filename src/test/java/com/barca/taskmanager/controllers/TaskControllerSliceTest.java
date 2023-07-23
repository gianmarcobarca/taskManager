package com.barca.taskmanager.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.barca.taskmanager.dtos.TaskCreationDto;
import com.barca.taskmanager.dtos.TaskDto;
import com.barca.taskmanager.services.TaskService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(controllers = TaskController.class)
public class TaskControllerSliceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskService taskService;

  @Test
  void getUserTasks_should_return_200() throws Exception {

    // arrange
    List<TaskDto> tasks = new ArrayList<>();
    // simulate sorting
    // TODO check json path for simulated sorting
    tasks.add(new TaskDto("2", "Task example 2", Instant.now().plusSeconds(1)));
    tasks.add(new TaskDto("1", "Task example 1", Instant.now()));

    Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");
    Page<TaskDto> page = new PageImpl<>(tasks);

    given(taskService.getUserTasks("user", pageable)).willReturn(page);

    // act and assert
    mockMvc
        .perform(get("/api/tasks")
            .with(jwt()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    // TODO add content.string(page), use ObjectMapper for generics

    // verify
    verify(taskService).getUserTasks("user", pageable);

  }

  @Test
  void getUserTasks_should_return_401() throws Exception {

    mockMvc
        .perform(get("/api/tasks"))
        .andDo(print())
        .andExpect(status().isUnauthorized());

  }

  @Test
  void createTask_should_return_201() throws Exception {

    TaskCreationDto dto = new TaskCreationDto("Task example");

    mockMvc
        .perform(post("/api/tasks")
            .with(jwt()) // sub = user, scope = read
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dto)))
        // .andDo(print())
        .andExpect(status().isCreated());

    verify(taskService).createTask("user", dto);
  }

  @Test
  void createTask_should_return_401() throws Exception {

    TaskCreationDto dto = new TaskCreationDto("Task example");

    mockMvc
        .perform(post("/api/tasks")
            .with(csrf().asHeader())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dto)))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void createTask_with_invalid_dto_should_return_400() throws Exception {

    // simulate invalid dto
    TaskCreationDto dto = new TaskCreationDto("");

    mockMvc
        .perform(post("/api/tasks")
            .with(jwt())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dto)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(
            result -> assertThat(result.getResolvedException() instanceof MethodArgumentNotValidException).isTrue());

  }

  @Test
  void createTask_with_null_body_should_return_400() throws Exception {

    mockMvc
        .perform(post("/api/tasks")
            .with(jwt()) // sub = user, scope = read
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            result -> assertThat(result.getResolvedException() instanceof HttpMessageConversionException).isTrue());
  }

  @Test
  void createTask_should_return_500() throws Exception {

    TaskCreationDto dto = new TaskCreationDto("Task example");

    // simulate arbitrary exception
    doThrow(RuntimeException.class)
        .when(taskService).createTask("user", dto);

    mockMvc
        .perform(post("/api/tasks")
            .with(jwt()) // sub = user, scope = read
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dto)))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void deleteTask_should_return_204() throws Exception {

    mockMvc
        .perform(delete("/api/tasks/{taskId}", "100")
            .with(jwt()))
        .andExpect(status().isNoContent());

    verify(taskService).deleteTask("user", "100");
  }

  @Test
  void deleteTask_should_return_401() throws Exception {

    mockMvc
        .perform(delete("/api/tasks/{taskId}", "100")
            .with(csrf().asHeader()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void deleteTask_should_return_404() throws Exception {

    doThrow(NoSuchElementException.class)
        .when(taskService).deleteTask("user", "100");

    mockMvc
        .perform(delete("/api/tasks/{taskId}", "100")
            .with(jwt()))
        .andExpect(status().isNotFound());

    verify(taskService).deleteTask("user", "100");
  }

  @Test
  void deleteTask_should_return_409() throws Exception {

    doThrow(DataIntegrityViolationException.class)
        .when(taskService).deleteTask("user", "100");

    mockMvc
        .perform(delete("/api/tasks/{taskId}", "100")
            .with(jwt()))
        .andExpect(status().isConflict());

    verify(taskService).deleteTask("user", "100");
  }

  @Test
  void deleteUserTasks_should_return_204() throws Exception {

    mockMvc
        .perform(delete("/api/tasks")
            .with(jwt()))
        .andExpect(status().isNoContent());

    verify(taskService).deleteUserTasks("user");
  }

  @Test
  void deleteUserTasks_should_return_401() throws Exception {

    mockMvc
        .perform(delete("/api/tasks")
            .with(csrf().asHeader()))
        .andExpect(status().isUnauthorized());
  }

  public static String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final String jsonContent = mapper.writeValueAsString(obj);
      return jsonContent;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
