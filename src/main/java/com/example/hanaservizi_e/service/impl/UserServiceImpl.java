package com.example.hanaservizi_e.service.impl;

import com.example.hanaservizi_e.repository.RolRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.hanaservizi_e.service.UserService;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.UserRepository;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl implements UserService {


    //Acceder a la DB//
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //Encriptar contraseñas//
    private final RolRepository rolRepository;

    public UserServiceImpl(UserRepository userRepository, RolRepository rolRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        //Buscar roles//
        this.rolRepository = rolRepository;

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registrarUsuario(User user){
        //Encriptar password//
        user.setPassword(passwordEncoder.encode((user.getPassword())));
        //Estado del usuario//
        user.setActive(true);
        //Creacion de usuario//
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
    @Override
    public Optional<User> buscarPorId(Long id){
        return userRepository.findById(id);
    }
    @Override
    public Optional<User> buscarPorNombre(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public Optional<User> buscarPorEmail(String email)
    {
        return userRepository.findByEmail(email);

    }
    @Override
    public List<User> buscarPorDireccion(String address)
    {
        return userRepository.findByAddress(address);

    }
    @Override
    public List<User> listarPorRol(String rolname){
        return userRepository.findByRolRolname(rolname);
    }

    @Override
    public List<User> buscarPorNombreOCorreo(String filtro)
    {
        return  userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(filtro,filtro);
    }
    @Override
    public List<User> listarTodos()
    {
        return userRepository.findAll();

    }

    @Override
    public boolean verificarPassword(String raw, String encoded) {
        return false;
    }

    @Override
    public String codificarPassword(String password) {
        return "";
    }

    @Override
    public void actualizar(User user) {

    }
}
