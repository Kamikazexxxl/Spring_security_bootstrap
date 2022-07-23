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
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService, UserServiceInterface {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

    }


    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
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

    @Transactional
    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void edit(User user) {
        User extracted = userRepository.findUserByEmail(user.getEmail()).get();
        if (user.getPassword() == "") {
            user.setPassword(extracted.getPassword());
        } else { user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void remove(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> {
            throw new UserNotExistsException("User with this email not exists");
        });
    }
}
