package com.example.hanaservizi_e;

import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.service.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;
import java.util.Collections;

public class CustomOidcUser extends DefaultOidcUser implements CustomUserDetails{

    private final User usuario;

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
                          OidcIdToken idToken,
                          OidcUserInfo userInfo,
                          User usuario) {
        super(authorities, idToken, userInfo);
        this.usuario = usuario;
    }

    @Bean
    public User getUsuario()
    {
        return usuario;
    }

    @Override
    public String getPassword() {
        return usuario.getPassword() != null ? usuario.getPassword() : "";
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(usuario.getRol().getRolname())
        );
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
        return usuario.isActive();
    }
}