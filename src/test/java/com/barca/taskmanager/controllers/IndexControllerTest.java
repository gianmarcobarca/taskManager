package com.barca.taskmanager.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.barca.taskmanager.configs.AuthSecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest(controllers = Index.class)
@Import(AuthSecurityConfig.class)
@ActiveProfiles("test")
class IndexControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void index_should_return_200() throws Exception {

    mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk());

    // TODO assert JsonPath
  }
}
