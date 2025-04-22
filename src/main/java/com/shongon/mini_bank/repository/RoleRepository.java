package com.shongon.mini_bank.repository;

import com.shongon.mini_bank.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByRoleName(String roleName);

    Optional<Role> findByRoleName(String roleName);

    void deleteByRoleName(String roleName);
}
