package com.example.hanaservizi_e.service.impl;

import com.example.hanaservizi_e.dto.VistaUsuarioDTO;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.RolRepository;
import com.example.hanaservizi_e.repository.UserRepository;
import com.example.hanaservizi_e.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    public UserServiceImpl(UserRepository userRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registrarUsuario(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public VistaUsuarioDTO convertirAUsuarioDto(User usuario) {
        return mapearUsuarioAUsuarioDto(usuario);
    }

    private VistaUsuarioDTO mapearUsuarioAUsuarioDto(User usuario) {
        VistaUsuarioDTO dto = new VistaUsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setPhone(usuario.getPhone());
        dto.setAddress(usuario.getAddress());
        dto.setRol(usuario.getRol().getRolname());

        if (usuario.getVendedor() != null) {
            dto.setCity(usuario.getVendedor().getCity());
        }

        return dto;
    }

    @Override
    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> buscarPorNombre(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> buscarPorDireccion(String address) {
        return userRepository.findByAddress(address);
    }

    @Override
    public List<User> listarPorRol(String rolname) {
        return userRepository.findByRolRolname(rolname);
    }

    @Override
    public List<User> buscarPorNombreOCorreo(String filtro) {
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(filtro, filtro);
    }

    @Override
    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    @Override
    public boolean verificarPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

    @Override
    public String codificarPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public void guardar(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void actualizar(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    @Override
    public void desactivarCuenta(Long idUsuario) {
        Optional<User> usuarioOpt = userRepository.findById(idUsuario);

        if (usuarioOpt.isPresent()) {
            User user = usuarioOpt.get();
            user.setActive(false);
            userRepository.save(user); // guarda el cambio
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
    }

    @Override
    public Optional<User> buscarInactivoPorEmail(String email){
        return userRepository.findByEmailAndIsActiveFalse(email);
    }

    @Override
    public Map<String, Long> obtenerUsuariosPorMes() {
        // Consulta a la base de datos (mes como número y cantidad)
        List<Object[]> resultados = userRepository.contarUsuariosPorMes();

        // Nombres de meses en español
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        // Usamos LinkedHashMap para mantener el orden
        Map<String, Long> usuariosPorMes = new LinkedHashMap<>();

        // Inicializamos todos los meses en 0
        for (String mes : meses) {
            usuariosPorMes.put(mes, 0L);
        }

        // Llenamos con los datos de la BD
        for (Object[] fila : resultados) {
            Integer numeroMes = ((Number) fila[0]).intValue(); // 1 = Enero, 2 = Febrero...
            Long cantidad = ((Number) fila[1]).longValue();

            String nombreMes = meses[numeroMes - 1]; // -1 porque el array es 0-based
            usuariosPorMes.put(nombreMes, cantidad);
        }

        return usuariosPorMes;
    }

}
