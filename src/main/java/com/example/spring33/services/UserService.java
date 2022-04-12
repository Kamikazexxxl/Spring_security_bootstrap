package com.example.spring33.services;

import com.example.spring33.exceptions.UserNotExistsException;
import com.example.spring33.models.Role;
import com.example.spring33.models.User;
import com.example.spring33.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        creatingInitUsers(userRepository);

    }

    private void creatingInitUsers(UserRepository userRepository) {
        Set<Role> temp = new HashSet<>();
        Role role = new Role();
        role.setRole("ADMIN");
        temp.add(role);
        User admin = new User();
        admin.setUsername("admin");
        admin.setName("admin");
        admin.setSurname("admin");
        admin.setPassword(passwordEncoder.encode("123"));
        admin.setAge(5);
        admin.setRoles(Set.of(new Role("ADMIN")));
        admin.setEmail("1@r.r");
        admin.setCredentialsNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setAccountNonExpired(true);
        admin.setEnabled(true);

        userRepository.save(admin);

        role = new Role();
        role.setRole("USER");
        temp.add(role);
        User user = new User();
        user.setUsername("us");
        user.setName("us");
        user.setSurname("us");
        user.setPassword(passwordEncoder.encode("123"));
        user.setAge(5);
        user.setRoles(Set.of(new Role("USER")));
        user.setEmail("2@r.r");
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setEnabled(true);

        userRepository.save(user);
//        role = new Role();
//        role.setRole("USER");
//        temp.add(role);
//        User user = new User("user", 40, "user@mail.ru",
//                "123", temp);
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//        userRepository.save(user);
//        temp.clear();
//        role = new Role();
//        role.setRole("ADMIN");
//        temp.add(role);
//        role = new Role();
//        role.setRole("USER");
//        temp.add(role);
//        User sup = new User("sup", 40, "sup@mail.ru",
//                "123", temp);
//        sup.setPassword(new BCryptPasswordEncoder().encode(sup.getPassword()));
//        userRepository.save(sup);
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
