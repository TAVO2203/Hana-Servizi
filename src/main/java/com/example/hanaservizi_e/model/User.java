package com.example.hanaservizi_e.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    @Column(unique = false, nullable = false)
    private String provider;

    @Column(nullable = false)
    private String phone;


    private String address;

    @Column(nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Vendedor vendedor;

    // 🔐 Implementación de UserDetails 🔐

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String rolBase = rol.getRolname().toUpperCase();
        String nombreRolConPrefijo = rolBase.startsWith("ROLE_") ? rolBase : "ROLE_" + rolBase;
        return Collections.singletonList(new SimpleGrantedAuthority(nombreRolConPrefijo));
    }


    @Override
    public String getUsername() {
        return this.username; // se usa el correo para iniciar sesión
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // puedes agregar lógica personalizada aquí
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
        return this.isActive;
    }
}
