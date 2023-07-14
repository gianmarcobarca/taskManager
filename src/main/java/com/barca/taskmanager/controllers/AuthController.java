package com.barca.taskmanager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.barca.taskmanager.dtos.JwtDto;
import com.barca.taskmanager.dtos.UserCreationDto;
import com.barca.taskmanager.services.AuthService;
import com.barca.taskmanager.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController("authController")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final UserService userService;

  @GetMapping("/token")
  public JwtDto getToken(Authentication auth) {
    return authService.createToken(auth);
  }

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public void signup(@RequestBody @Valid UserCreationDto dto) {
    userService.createUser(dto);
  }
}
