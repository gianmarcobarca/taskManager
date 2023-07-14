package com.barca.taskmanager.controllers;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // TODO: Extend ErrorResponse

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    return ProblemDetail.forStatus(HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationException(MethodArgumentNotValidException e) {
    return e.getBody();
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ProblemDetail handleNoSuchElementException(NoSuchElementException e) {
    return ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleAccessDeniedException(AccessDeniedException e) {
    return ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleExceptions() {
    return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
