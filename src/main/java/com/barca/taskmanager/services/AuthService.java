package com.barca.taskmanager.services;

import org.springframework.security.core.Authentication;

import com.barca.taskmanager.dtos.JwtDto;

public interface AuthService {

  JwtDto createToken(Authentication auth);

}
