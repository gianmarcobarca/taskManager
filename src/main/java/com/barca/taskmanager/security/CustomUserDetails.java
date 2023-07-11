package com.barca.taskmanager.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {

  String getId();

  String getName();
}
