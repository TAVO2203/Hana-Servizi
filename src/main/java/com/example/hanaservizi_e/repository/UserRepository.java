package com.example.hanaservizi_e.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hanaservizi_e.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByAddress(String address);
    List<User> findByRolRolname(String rolname);
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);
    Optional<User> findByEmailAndIsActiveFalse(String email);

    @Query("SELECT FUNCTION('MONTH', u.createdAt) as mes, COUNT(u) as cantidad " +
            "FROM User u GROUP BY FUNCTION('MONTH', u.createdAt) ORDER BY mes")
    List<Object[]> contarUsuariosPorMes();

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
