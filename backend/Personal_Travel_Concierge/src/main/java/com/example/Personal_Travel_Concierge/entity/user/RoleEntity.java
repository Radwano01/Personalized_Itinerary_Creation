package com.example.Personal_Travel_Concierge.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String role;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<UserEntity> users = new ArrayList<>();


    public RoleEntity(String role){
        this.role = role;
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "role='" + role + '\'' +
                '}';
    }
}