package com.example.spring.services;

import com.example.spring.exceptions.UserNotExistsException;
import com.example.spring.models.Role;
import com.example.spring.models.User;
import com.example.spring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UserService implements UserDetailsService, UserServiseInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

    }


    @Override
    @Transactional
    public User findUserByUserName(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public User findUserByEmail(String username) {

        return userRepository.findUserByEmail(username).get();
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUserName(username);
        if (user == null) {
            user = findUserByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("User %s not found", username));
            }
        }
        return user;
    }

    @Override
    public Optional<User> findById(long id) {

        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        System.out.println(user);
//        User extracted = userRepository.findUserByEmail(user.getEmail()).or();
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists!");
        } else {
            if (user.getRoles() == null) {
                Set<Role> defaultRole = new HashSet<>();
                defaultRole.add(new Role("USER"));
                user.setRoles(defaultRole);
            }
            user.setUsername(user.getName() + " " + user.getEmail());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAccountNonLocked(true);
            user.setAccountNonExpired(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    @Override
    public void edit(User user) {
        User extracted = userRepository.findUserByEmail(user.getEmail()).get();
        if (user.getRoles() == null) {
            Set<Role> defaultRole = new HashSet<>();
            defaultRole.add(new Role("USER"));
            user.setRoles(defaultRole);
        }
        if (user.getPassword() == null) {
            user.setPassword(extracted.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setUsername(user.getName() + " " + user.getEmail());
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void remove(long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> {
            throw new UserNotExistsException("User with this email not exists");
        });
    }
}
