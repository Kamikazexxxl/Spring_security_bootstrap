package com.example.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {


    @Id
    @GeneratedValue
    @Getter
    private long id;

    @Getter
    private String role;

    public String getAuthority() {
        return getRole();
    }


    public Role() {
    }

    public Role(String role) {
        this.role = role;
    }

    public Role(long id, String role) {
        this.id = id;
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
