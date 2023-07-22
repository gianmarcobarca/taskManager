package com.barca.taskmanager.models;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Document(collection = "tasks")
@Value
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

  public Task(String id, String content, Instant createdDate, String userId) {
    this.id = id;
    this.content = content.trim();
    this.createdDate = createdDate;
    this.userId = userId;
  }

  @Builder
  public static Task of(String content, String userId) {
    return new Task(null, content, null, userId);

  }
}
