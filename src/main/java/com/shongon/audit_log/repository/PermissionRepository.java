package com.shongon.audit_log.repository;

import com.shongon.audit_log.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByPermissionName(String permissionName);

    Optional<Permission> findByPermissionName(String permName);
}
