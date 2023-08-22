package com.barca.taskmanager.services.internal;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.barca.taskmanager.dtos.JwtDto;
import com.barca.taskmanager.security.CustomUserDetails;
import com.barca.taskmanager.services.AuthService;

import lombok.RequiredArgsConstructor;

@Service("authService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final JwtEncoder jwtEncoder;

  @Override
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
        .subject(((CustomUserDetails) auth.getPrincipal()).getId())
        .claim("name", ((CustomUserDetails) auth.getPrincipal()).getName())
        .claim("email", ((UserDetails) auth.getPrincipal()).getUsername())
        .claim("scope", scope)
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plusSeconds(expiry))
        .build();

    return new JwtDto(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
  }

}
