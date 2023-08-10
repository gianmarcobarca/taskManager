package com.barca.taskmanager.controllers;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.barca.taskmanager.configs.AuthSecurityConfig;
import com.barca.taskmanager.dtos.JwtDto;
import com.barca.taskmanager.dtos.UserCreationDto;
import com.barca.taskmanager.services.AuthService;
import com.barca.taskmanager.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doThrow;
import static org.assertj.core.api.Assertions.*;

@WebMvcTest(controllers = AuthController.class)
@Import(AuthSecurityConfig.class)
class AuthControllerSliceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;
  @MockBean
  private UserService userService;

  @Test
  @WithMockUser(username = "username", password = "password")
  void getToken_should_return_200() throws Exception {

    JwtDto dto = new JwtDto("Token Example");
    Authentication auth = TestSecurityContextHolder.getContext().getAuthentication();

    given(authService.createToken(auth)).willReturn(dto);

    mockMvc
        .perform(get("/auth/token"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(asJsonString(dto)));

    verify(authService).createToken(auth);

  }

  @Test
  void getToken_should_return_401() throws Exception {

    mockMvc
        .perform(get("/auth/token"))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  void signup_should_return_200() throws Exception {

    UserCreationDto dto = new UserCreationDto("john", "doe", "johndoe@example.com", "password");

    mockMvc
        .perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dto)))
        .andDo(print())
        .andExpect(status().isCreated());

    verify(userService).createUser(dto);

  }

  @Test
  void signup_with_invalid_dto_should_return_400() throws Exception {

    UserCreationDto dto = new UserCreationDto(null, null, null, null);

    mockMvc
        .perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dto)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertThat(result.getResolvedException())
            .isInstanceOf(MethodArgumentNotValidException.class));
  }

  @Test
  void signup_with_null_body_should_return_400() throws Exception {

    mockMvc
        .perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertThat(result.getResolvedException())
            .isInstanceOf(HttpMessageConversionException.class));
  }

  @Test
  void signup_should_return_500() throws Exception {

    UserCreationDto dto = new UserCreationDto("john", "doe", "johndoe@example.com", "password");

    doThrow(RuntimeException.class)
        .when(userService).createUser(dto);

    mockMvc
        .perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dto)))
        .andDo(print())
        .andExpect(status().isInternalServerError());

    verify(userService).createUser(dto);

  }

  private static String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final String jsonContent = mapper.writeValueAsString(obj);
      return jsonContent;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
