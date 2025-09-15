package com.example.hanaservizi_e;

import com.example.hanaservizi_e.model.Rol;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.RolRepository;
import com.example.hanaservizi_e.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public CustomOidcUserService(UserRepository userRepository, RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // Obtener el usuario desde Google (OIDC)
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String name = oidcUser.getAttribute("name");

        // Buscar usuario en BD o crearlo
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseGet(() -> crearUsuario(email, name));

        if (!user.isActive()) {
            throw new DisabledException("Cuenta desactivada");
        }


        // Authorities según el rol en BD
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(user.getRol().getRolname()));

        // En lugar de devolver un DefaultOidcUser plano, devolvemos un CustomOidcUser
        return new CustomOidcUser(
                authorities,
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                user // nuestra entidad User de BD
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
        newUser.setActive(true); // activar al crearlo
        return userRepository.save(newUser);
    }
}