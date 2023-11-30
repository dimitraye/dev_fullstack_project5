package com.openclassrooms.starterjwt.security.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import com.openclassrooms.starterjwt.config.config.ExcludeFromJacocoGeneratedReport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;

  private String firstName;

  private String lastName;

  private Boolean admin;

  @JsonIgnore
  private String password;  
  
  public Collection<? extends GrantedAuthority> getAuthorities() {        
      return new HashSet<GrantedAuthority>();
  }

  @Override
  @ExcludeFromJacocoGeneratedReport
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @ExcludeFromJacocoGeneratedReport
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @ExcludeFromJacocoGeneratedReport
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @ExcludeFromJacocoGeneratedReport
  public boolean isEnabled() {
    return true;
  }

  @Override
  @ExcludeFromJacocoGeneratedReport
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  } 
}
