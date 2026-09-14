package com.aramdev.delivery.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long userId;
    private String username;
    private String password;
    private List<? extends GrantedAuthority> authorities;

    public boolean hasAuthority(String authority) {
        return authorities.stream()
                .anyMatch(a ->
                        Objects.equals(a.getAuthority(), authority)
                );
    }

}