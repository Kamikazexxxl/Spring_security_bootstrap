package com.example.spring33.models;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Component
@ToString
//@Scope("session")
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private long id;

    @Column(nullable = true, unique = true)
    @Setter
    @Getter
    @Size(min = 4, max = 20)
    private String username;

    @Column(nullable = false)
    @Size(min = 4, max = 20)
    @Setter
    @Getter
    private String name;

    @Column/*(nullable = false)*/
    @Setter
    @Getter
    @Size(min = 4, max = 20)
    private String surname;

    @Column/*(nullable = false)*/
    @Setter
    @Getter
    private int age;

    @Column(unique = true)
    @Setter
    @Getter
    private String email;

    @Column(nullable = false)
    @Setter
    @Getter
    @Size(min = 3)
    private String password;

    @Column()
    @ColumnDefault(value = "true")
    @Getter
    @Setter
    private Boolean accountNonExpired;

    @Column()
    @ColumnDefault(value = "true")
    @Getter
    @Setter
    private Boolean accountNonLocked;

    @Column()
    @ColumnDefault(value = "true")
    @Getter
    @Setter
    private Boolean credentialsNonExpired;

    @Column()
    @Getter
    @Setter
    @ColumnDefault(value = "true")
    private Boolean enabled;

    @Getter
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Role> roles;

    public User(String username, String name, String surname, int age, String email, String password, Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired, Boolean enabled, Set<Role> roles) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(el -> new SimpleGrantedAuthority("ROLE_" + el.getAuthority())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setRoles(String roles) {
        if (roles != null && !roles.isEmpty()) {
            String[] arrayRoles = roles.split("[^A-Za-zФ-Яа-я0-9]");
            if (arrayRoles.length != 0) {
                Set<Role> roleSet = new HashSet<>();
                Arrays.stream(arrayRoles).forEach(el -> roleSet.add(new Role(el)));
                this.roles = roleSet;
            } else {
                this.roles = new HashSet<>();
                this.roles.add(new Role(roles));
            }

        }

    }

    public String showPrettyViewOfRoles() {
        return roles.stream().map(Role::getRole).collect(Collectors.joining(" | "));
    }


}
