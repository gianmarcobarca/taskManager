package com.barca.taskmanager.dtos;

import java.time.Instant;

public record TaskDto(String id, String content, Instant createdDate) {

}
