package com.barca.taskmanager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.barca.taskmanager.annotations.Principal;
import com.barca.taskmanager.dtos.JwtDto;
import com.barca.taskmanager.dtos.UserCreationDto;
import com.barca.taskmanager.security.CustomUserDetails;
import com.barca.taskmanager.services.TokenService;
import com.barca.taskmanager.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController("authController")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final TokenService tokenService; // Rename to token service
  private final UserService userService;

  @GetMapping("/token")
  public JwtDto getToken(@Principal UserDetails userDetails) {
    return tokenService.createToken((CustomUserDetails) userDetails);
  }

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public void signup(@RequestBody @Valid UserCreationDto dto) {
    userService.createUser(dto);
  }

  // TODO changePassword

  @DeleteMapping("/deregister")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deregister(@Principal UserDetails userDetails) {
    userService.deleteUser(((CustomUserDetails) userDetails).getId());
  }

}
