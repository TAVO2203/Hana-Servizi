package com.example.hanaservizi_e.service.impl;

import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.service.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetailsImpl implements CustomUserDetails {

    private final User user;

    public CustomUserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public User getUsuario() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(user.getRol().getRolname())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return user.isActive(); }
}
