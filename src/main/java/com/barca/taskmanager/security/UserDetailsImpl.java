package com.barca.taskmanager.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.barca.taskmanager.models.User;

import lombok.Data;

@Data
public class UserDetailsImpl implements CustomUserDetails {

  private final User user;

  @Override
  public String getId() {
    return user.getId();
  }

  @Override
  public String getName() {
    return user.getFirstName() + " " + user.getLastName();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<String> authorities = user.getAuthorities();
    Set<GrantedAuthority> copy = new HashSet<>();

    for (String authority : authorities) {
      copy.add(new SimpleGrantedAuthority(authority));
    }
    return copy;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
