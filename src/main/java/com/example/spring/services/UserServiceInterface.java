package com.example.spring.services;

import com.example.spring.models.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    User findUserByUserName(String username);

    List<User> findAll();

    Optional<User> findById(long id);

    void save(User user);

    void edit(User user);

    void remove(long id);
    User findUserByEmail(String username);
    User findByEmail(String email);

}
