package com.shongon.mini_bank.service.security;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shongon.mini_bank.dto.response.audit_log.AuditLogResponse;
import com.shongon.mini_bank.exception.AppException;
import com.shongon.mini_bank.exception.ErrorCode;
import com.shongon.mini_bank.mapper.AuditLogMapper;
import com.shongon.mini_bank.model.AuditLog;
import com.shongon.mini_bank.model.User;
import com.shongon.mini_bank.repository.AuditLogRepository;
import com.shongon.mini_bank.repository.UserRepository;
import com.shongon.mini_bank.utils.CustomTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogService {
    AuditLogRepository auditLogRepository;
    AuditLogMapper auditLogMapper;
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getUserLogsPaginated(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Page<AuditLog> logPage = auditLogRepository.findByUser_UserId(userId, pageable);

        return logPage.map(auditLogMapper::toAuditLogResponse);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAllLogsPaginated(Pageable pageable) {
        Page<AuditLog> logPage = auditLogRepository.findAll(pageable);
        return logPage.map(auditLogMapper::toAuditLogResponse);
    }

    // Lấy log theo entityName theo trang và sắp xếp
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getEntityLogsPaginated(String entityName, Pageable pageable) {
        Page<AuditLog> logPage = auditLogRepository.findByEntityName(entityName, pageable);
        return logPage.map(auditLogMapper::toAuditLogResponse);
    }

//    ******************************************************************************************
    // Log methods
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAfterCommit(Long userId, String action, String entityName, Long entityId, String details) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user != null) {
            logAction(user, action, entityName, entityId, details);
        } else {
            log.warn("Cannot log action for non-existent user with ID: {}", userId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(User user, String action, String entityName, Long entityId, String details) {
        AuditLog auditLog = AuditLog.builder()
                .user(user)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(auditLog);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String action, String entityName, Long entityId, String details) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            logAction(currentUser, action, entityName, entityId, details);
        }
    }

    // Automation deletes the "AUTHENTICATE" action after 3 days - timestamp
    @Scheduled(fixedRate = 3 * 24 * 60 * 60 * 1000)
    @Transactional
    public void cleanupOldAuthenticationLogs() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(3);
        log.info("Starting scheduled task: Deleting AUTHENTICATE logs older than {}", cutoffDate);
        try {
            int deletedCount = auditLogRepository.deleteOldLogsByAction("AUTHENTICATE", cutoffDate);
            log.info("Finished scheduled task: Deleted {} AUTHENTICATE logs older than {}", deletedCount, cutoffDate);
        } catch (Exception e) {
            log.error("Error during scheduled deletion of old authentication logs", e);
        }
    }

    private User getCurrentUser() {
        try {
            var securityContext = SecurityContextHolder.getContext();
            var authentication = securityContext.getAuthentication();

            if (authentication != null) {
                String username = authentication.getName();
                if (username != null && !username.equals("anonymousUser")) {
                    return userRepository.findByUsername(username).orElse(null);
                }
            }
        } catch (Exception e) {
            log.error("Error while getting current user: {}", e.getMessage());
        }
        return null;
    }
}
