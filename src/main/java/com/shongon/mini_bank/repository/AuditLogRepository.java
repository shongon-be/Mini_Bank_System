package com.shongon.mini_bank.repository;

import com.shongon.mini_bank.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUser_UserId(Long userUserId);
    List<AuditLog> findByEntityName(String entityName);

    void deleteByUser_UserId(Long userId);

}
