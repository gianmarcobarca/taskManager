package com.barca.taskmanager.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public final class UserCreationDto {

  @NotNull
  @Size(min = 1, max = 50)
  private final String firstName;
  @NotNull
  @Size(min = 1, max = 50)
  private final String lastName;
  @NotNull
  @Size(min = 6, max = 50)
  @Email
  private final String email;
  @NotNull
  @Size(min = 8, max = 24)
  private final String password;
}
