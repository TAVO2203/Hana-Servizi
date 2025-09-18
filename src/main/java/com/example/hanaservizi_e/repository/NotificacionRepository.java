package com.example.hanaservizi_e.repository;

import com.example.hanaservizi_e.model.Notificacion;
import com.example.hanaservizi_e.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioAndLeidoFalse(User usuario);
    List<Notificacion> findByUsuarioOrderByFechaDesc(User usuario);

    @Query("SELECT n FROM Notificacion n WHERE n.usuario = :usuario ORDER BY n.fecha DESC")
    List<Notificacion> findByUsuario(@Param("usuario") User usuario);


}

