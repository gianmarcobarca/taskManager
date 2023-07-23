package com.barca.taskmanager.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barca.taskmanager.dtos.UserCreationDto;
import com.barca.taskmanager.models.User;
import com.barca.taskmanager.repositorites.UserRepository;
import com.barca.taskmanager.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service("userService")
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new UserDetailsImpl(userRepository.findByEmail(username).orElseThrow());
  }

  @Override
  public void createUser(UserCreationDto dto) {

    var copy = User
        .builder()
        .firstName("")
        .lastName(dto.getLastName())
        .email(dto.getEmail())
        .password(passwordEncoder.encode(dto.getPassword()))
        .build();

    userRepository.save(copy);
  }
}
