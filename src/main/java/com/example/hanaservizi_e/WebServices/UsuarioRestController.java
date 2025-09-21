package com.example.hanaservizi_e.WebServices;

import com.example.hanaservizi_e.model.Rol;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.RolRepository;
import com.example.hanaservizi_e.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ws/users")
public class UsuarioRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/buscar")
    public Optional<User> findByUsername(@RequestParam String username) {
        return userRepository.findAll().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    @PostMapping("/crear")
    public ResponseEntity<?> create(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("El nombre de usuario ya está en uso");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("El email ya está en uso");
        }

        if (user.getRol() == null || user.getRol().getId() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Debes enviar un rol válido");
        }

        Rol rol = rolRepository.findById(user.getRol().getId())
                .orElse(null);
        if (rol == null) {
            return ResponseEntity
                    .badRequest()
                    .body("El rol con id " + user.getRol().getId() + " no existe");
        }

        user.setRol(rol);
        user.setCreatedAt(LocalDateTime.now());


        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/eliminar/{id}")
    public String delete(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "Usuario elimanado con éxito: " + id;
        } else {
            return "Usuario no encontrado: " + id;
        }
    }

    @GetMapping("/crearEjemplo")
    public User crearEjemplo() {
        User user = new User();
        user.setUsername("carlitos");
        user.setPassword("carlis123");
        user.setEmail("carlos123@gmail.com");
        user.setAddress("cra 111c #81-30");
        user.setPhone("3192350806");
        user.setCreatedAt(java.time.LocalDateTime.now());

        Rol rol = rolRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("El rol con id 1 no existe"));

        user.setRol(rol);

        return userRepository.save(user);
    }

    @GetMapping("/eliminarEjemplo/{id}")
    public String eliminarEjemplo(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "Usuario eliminado con éxito: " + id;
        } else {
            return "Usuario no encontrado: " + id;
        }
    }
}