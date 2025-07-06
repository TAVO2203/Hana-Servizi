package com.example.hanaservizi_e.service;


import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registrarUsuario(User user);

    Optional<User> buscarPorId(Long id);

    Optional<User> buscarPorNombre(String username);

    Optional<User> buscarPorEmail(String email);

    List<User> buscarPorDireccion(String address);

    List<User> listarPorRol(String rolnombre);

    List<User> buscarPorNombreOCorreo(String filtro);

    List<User> listarTodos();

    boolean verificarPassword(String raw, String encoded);

    String codificarPassword(String password);

    void guardar(User user); // guarda cambios y actualiza updatedAt

    void actualizar(User user); // opcionalmente igual a guardar()
}

