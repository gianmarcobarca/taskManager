package com.barca.taskmanager.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public final class TaskDto {
  private final String id;
  @NotNull
  @Size(min = 1, max = 200)
  private final String content;
}
