package com.example.spring33.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
@Data
//@NoArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue
    private long id;

   // @ManyToMany(mappedBy = "users")
    @Column
    private String role;

    @Override
    public String getAuthority() {
        return getRole();
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
        return  role;
    }
}
