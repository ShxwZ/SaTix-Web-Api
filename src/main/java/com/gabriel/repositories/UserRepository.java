package com.gabriel.repositories;

import com.gabriel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository
        extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.typeUser = 'ADMIN'")
    Optional<User> getUserAdminByUsername(String username);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = ?1")
    Boolean existsByUsername(String username);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = ?1")
    Boolean existsByEmail(String email);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.dni = ?1")
    Boolean existsByDni(String dni);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.phone = ?1")
    Boolean existsByPhone(String phone);
}
