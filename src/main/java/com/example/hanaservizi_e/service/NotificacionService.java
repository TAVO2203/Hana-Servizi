package com.example.hanaservizi_e.service;

import com.example.hanaservizi_e.model.Notificacion;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepository repo;


    public void crearNotificacion(User usuario, String titulo, String mensaje) {
        Notificacion noti = new Notificacion();
        noti.setUsuario(usuario);
        noti.setTitulo(titulo);
        noti.setMensaje(mensaje);
        noti.setFecha(LocalDateTime.now());
        noti.setLeido(false);
        repo.save(noti);
    }
    public List<Notificacion> obtenerTodasPorUsuarioOrdenadas(User usuario) {
        return repo.findByUsuarioOrderByFechaDesc(usuario);
    }

    public List<Notificacion> obtenerNoLeidas(User usuario) {
        return repo.findByUsuarioAndLeidoFalse(usuario);
    }
    
    public List<Notificacion> obtenerPorUsuario(User usuario) {
        return repo.findByUsuario(usuario);
    }


    public void marcarComoLeida(Long id) {
        Notificacion n = repo.findById(id).orElseThrow();
        n.setLeido(true);
        repo.save(n);
    }

    public void eliminarPorId(Long id) {
        repo.deleteById(id);
    }
}

