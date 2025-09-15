    package com.example.hanaservizi_e.service;

    import com.example.hanaservizi_e.model.User;
    import com.example.hanaservizi_e.repository.UserRepository;
    import com.example.hanaservizi_e.service.impl.CustomUserDetailsImpl;
    import jakarta.transaction.Transactional;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;
    //import org.springframework.stereotype.Service;

    import java.util.Collections;

    @Service

    public class CustomUserDetailsService implements UserDetailsService {

      private final UserRepository userRepository;

      public CustomUserDetailsService(UserRepository userRepository)
      {
          this.userRepository = userRepository;
      }

      @Override
      @Transactional
      public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
          User user = userRepository.findByEmail(email)
                  .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

          // Prefijo ROLE_ para Spring Security
          String authority = user.getRol().getRolname();

          // Si el usuario viene de Google y no tiene password, usamos ""
          String password = user.getPassword() != null ? user.getPassword() : "";

          return new CustomUserDetailsImpl(user);

      }
    }

