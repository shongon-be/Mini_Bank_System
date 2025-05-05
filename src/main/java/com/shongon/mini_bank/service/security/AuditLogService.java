package com.shongon.mini_bank.service.security;

import com.shongon.mini_bank.dto.response.audit_log.AuditLogResponse;
import com.shongon.mini_bank.exception.AppException;
import com.shongon.mini_bank.exception.ErrorCode;
import com.shongon.mini_bank.mapper.AuditLogMapper;
import com.shongon.mini_bank.model.AuditLog;
import com.shongon.mini_bank.model.User;
import com.shongon.mini_bank.repository.AuditLogRepository;
import com.shongon.mini_bank.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogService {
    AuditLogRepository auditLogRepository;
    AuditLogMapper auditLogMapper;
    UserRepository userRepository;

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
    public void logRegisterAction(String username, String action, String entityName, Long entityId, String details) {
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user != null) {
            logAction(user, action, entityName, entityId, details);
        } else {
            log.warn("Cannot log action for non-existent user: {}", username);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String action, String entityName, Long entityId, String details) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            logAction(currentUser, action, entityName, entityId, details);
        }
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> getUserLogs(Long userId) {
        List<AuditLog> logs = auditLogRepository.findByUser_UserId(userId);
        if (logs.isEmpty()) {
            throw new AppException(ErrorCode.USER_LOGS_NOT_FOUND);
        }
        return logs.stream()
                .map(auditLogMapper::toAuditLogResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuditLogResponse> getEntityLogs(String entityName) {
        return auditLogRepository.findByEntityName(entityName)
                .stream()
                .map(auditLogMapper::toAuditLogResponse)
                .toList();
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
