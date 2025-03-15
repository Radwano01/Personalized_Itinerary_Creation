package com.example.Personal_Travel_Concierge.repository.user;

import com.example.Personal_Travel_Concierge.entity.user.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByRole(String role);
    boolean existsByRole(String role);
}
