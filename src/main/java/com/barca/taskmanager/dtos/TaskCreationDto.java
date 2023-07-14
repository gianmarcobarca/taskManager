package com.barca.taskmanager.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskCreationDto(@NotNull @Size(min = 1, max = 200) String content) {

  public TaskCreationDto {
    content = content.trim();
  }
}
