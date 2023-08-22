package com.barca.taskmanager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.barca.taskmanager.dtos.AppInfoDto;

@RestController
public class Index {

  @GetMapping("/")
  @ResponseStatus(HttpStatus.OK)
  public AppInfoDto index() {
    return new AppInfoDto("TaskManager", "Gianmarco Barca");
  }
}
