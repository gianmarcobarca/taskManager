package com.barca.taskmanager.models;

import java.util.Collections;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Document(collection = "users")
@Data
public class User {

  @Id
  private final String id;
  @Field("first_name")
  @Size(min = 1, max = 50)
  @NotNull
  private final String firstName;
  @Field("last_name")
  @NotNull
  @Size(min = 1, max = 50)
  private final String lastName;
  @Indexed(unique = true)
  @NotNull
  @Size(min = 6, max = 50)
  @Email
  private final String email;
  @NotNull
  @Size(min = 8, max = 24)
  private final String password;
  @NotNull
  private final Set<String> authorities;

  public User( // private access will cause performance loss
      String id,
      String firstName,
      String lastName,
      String email,
      String password,
      Set<String> authorities) {

    this.id = id;
    this.firstName = firstName.toLowerCase().trim();
    this.lastName = lastName.toLowerCase().trim();
    this.email = email.toLowerCase();
    this.password = password;
    this.authorities = authorities;
  }

  public static User of(
      String firstName,
      String lastName,
      String email,
      String password) {

    return new User(
        null,
        firstName,
        lastName,
        email,
        password,
        Collections.emptySet());
  }
}
