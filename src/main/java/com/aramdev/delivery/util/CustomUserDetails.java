package com.aramdev.delivery.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private String username;
    private String password;
    private List<? extends GrantedAuthority> authorities;
}