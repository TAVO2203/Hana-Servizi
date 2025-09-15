package com.example.hanaservizi_e.service;

import com.example.hanaservizi_e.model.User;
        import org.springframework.security.core.GrantedAuthority;
        import org.springframework.security.core.authority.SimpleGrantedAuthority;
        import org.springframework.security.core.userdetails.UserDetails;

        import java.util.Collection;
        import java.util.Collections;

public interface CustomUserDetails extends UserDetails {

    User getUsuario();


}

