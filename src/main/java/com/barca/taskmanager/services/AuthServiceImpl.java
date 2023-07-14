package com.barca.taskmanager.services;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.barca.taskmanager.dtos.JwtDto;
import com.barca.taskmanager.models.Task;
import com.barca.taskmanager.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service("authService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final JwtEncoder jwtEncoder;
  private final PasswordEncoder passwordEncoder;

  @Override
  @PreAuthorize("isAuthenticated()")
  public JwtDto createToken(Authentication auth) {

    Instant now = Instant.now();
    long expiry = 3600L; // 1h

    String scope = auth
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));

    JwtClaimsSet claims = JwtClaimsSet
        .builder()
        .subject(getUserDetails(auth).getId())
        .claim("name", getUserDetails(auth).getName())
        .claim("scope", scope)
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plusSeconds(expiry))
        .build();

    return new JwtDto(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
  }

  @Override
  public String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

  @Override // only for HTTP Basic
  public CustomUserDetails getUserDetails(Authentication auth) {
    return (CustomUserDetails) auth.getPrincipal();
  }

  @Override
  public void checkTaskUserId(String userId, Task task) {
    if (!(userId.equals(task.getUserId()))) {
      throw new AccessDeniedException("Access denied.");
    }
  }
}
