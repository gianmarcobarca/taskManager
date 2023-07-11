package com.barca.taskmanager.models;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Document(collection = "tasks")
@Data
public class Task {

  @Id
  private final String id;
  @NotNull
  @Size(min = 1, max = 200)
  private final String content;
  @Field("created_date")
  @CreatedDate
  private final Instant createdDate;
  @Field("user_id")
  @NotNull
  private final String userId;

}
