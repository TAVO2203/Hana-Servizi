package com.example.hanaservizi_e;

import com.example.hanaservizi_e.model.Rol;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.RolRepository;
import com.example.hanaservizi_e.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");


        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseGet(() -> crearUsuario(email, name));




        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(user.getRol().getRolname()));

        return new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                "email"
        );
    }

    private User crearUsuario(String email, String name) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(name);
        newUser.setProvider("GOOGLE");
        newUser.setCreatedAt(LocalDateTime.now());

        System.out.println("Buscando rol ROLE_CLIENTE...");
        Rol rolCliente = rolRepository.findByRolname("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
        System.out.println("Rol encontrado: " + rolCliente.getRolname());

        newUser.setRol(rolCliente);
        return userRepository.save(newUser);
    }
}
