package com.example.spring33.repositories;

import com.example.spring33.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findUserByEmail(String email);
}
