package com.barca.taskmanager.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component("authenticationEvents")
@Log4j2
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
