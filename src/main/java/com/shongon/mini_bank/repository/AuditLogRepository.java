package com.shongon.mini_bank.repository;

import com.shongon.mini_bank.model.AuditLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByUser_UserId(Long userId, Pageable pageable);

    Page<AuditLog> findByEntityName(String entityName, Pageable pageable);

    @Modifying
    void deleteByUser_UserId(Long userId);

    @Modifying
    @Query("DELETE FROM AuditLog a WHERE a.action = :action AND a.timestamp < :cutoffDate")
    int deleteOldLogsByAction(@Param("action") String action, @Param("cutoffDate") LocalDateTime cutoffDate);
}
