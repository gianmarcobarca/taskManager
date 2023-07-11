package com.barca.taskmanager.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("authenticationEvents")
@Slf4j
public class AuthenticationEvents {
  @EventListener
  public void onFailure(AbstractAuthenticationFailureEvent failure) {
    log.info(failure.toString());
  }

  @EventListener
  public void onFailure(AuthorizationDeniedEvent<?> failure) {
    log.info(failure.getAuthorizationDecision().toString());
  }
}
