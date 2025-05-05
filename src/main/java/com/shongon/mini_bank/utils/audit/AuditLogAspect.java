package com.shongon.mini_bank.utils.audit;

import com.shongon.mini_bank.repository.UserRepository;
import com.shongon.mini_bank.service.security.AuditLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuditLogAspect {
    AuditLogService auditLogService;
    ApplicationContext applicationContext;
    
    @Pointcut("@annotation(com.shongon.mini_bank.utils.audit.Auditable)")
    public void auditableMethod() {}

    @AfterReturning(pointcut = "auditableMethod()", returning = "result")
    public void logAuditableMethod(JoinPoint joinPoint, Object result) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Auditable auditable = method.getAnnotation(Auditable.class);

            if (auditable != null) {
                String action = auditable.action();
                String entityType = auditable.entityType();
                Long entityId = null;

                if (auditable.isAutoDetectId()) {
                    entityId = extractEntityId(result, joinPoint.getArgs(), entityType);
                }

                String details = "Method " + method.getName() + " from " +
                        method.getDeclaringClass().getSimpleName();

                auditLogService.logAction(action, entityType, entityId, details);
            } else {
                log.warn("Method {} in {} matched Auditable pointcut but is not annotated with @Auditable!",
                        method.getName(), method.getDeclaringClass().getSimpleName());
            }
        } catch (Exception e) {
            log.error("Error in logAuditableMethod: {}", e.getMessage(), e);
        }
    }

    private Long extractEntityId(Object result, Object[] args, String entityType) {
        // Try to get from the result first
        if (result != null) {
            // If the result is a collection, try to get user IDs from elements
            if (result instanceof Collection<?> collection && !collection.isEmpty()) {
                return collection.stream()
                        .findFirst()
                        .map(this::getIdFromObject)
                        .orElse(null);
            }
            Long id = getIdFromObject(result);
            if (id != null) return id;
        }

        // Try to get from arguments
        for (Object arg : args) {
            if (arg == null) continue;

            // If direct Long ID
            if (arg instanceof Long longId) {
                return longId;
            }

            // If argument contains username field (for auth operations)
            String username = extractUsername(arg);
            if (username != null && entityType.equals("User")) {
                return getUserIdFromUsername(username);
            }

            // Try to get ID from the object
            Long id = getIdFromObject(arg);
            if (id != null) return id;

            // Check if it's a request object containing ID
            if (arg.getClass().getSimpleName().contains(entityType) &&
                    arg.getClass().getSimpleName().contains("Request")) {
                Long requestId = extractIdFromRequest(arg);
                if (requestId != null) return requestId;
            }
        }

        return null;
    }

    private String extractUsername(Object arg) {
        try {
            Method getUsername = arg.getClass().getMethod("getUsername");
            Object result = getUsername.invoke(arg);
            return result instanceof String ? (String) result : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private Long extractIdFromRequest(Object arg) {
        try {
            Method getId = arg.getClass().getMethod("getId");
            Object result = getId.invoke(arg);
            return result instanceof Long ? (Long) result : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private Long getUserIdFromUsername(String username) {
        try {
            UserRepository userRepository = applicationContext.getBean(UserRepository.class);
            return userRepository.findByUsername(username)
                    .map(user -> {
                        try {
                            Method getUserId = user.getClass().getMethod("getUserId");
                            Object result = getUserId.invoke(user);
                            return result instanceof Long ? (Long) result : null;
                        } catch (Exception e) {
                            log.error("Error getting userId from user object: {}", e.getMessage());
                            return null;
                        }
                    })
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error getting user ID from username: {}", e.getMessage());
            return null;
        }
    }

    private Long getIdFromObject(Object obj) {
        String[] idMethodNames = {"getId", "get" + obj.getClass().getSimpleName() + "Id"};

        for (String methodName : idMethodNames) {
            try {
                Method method = obj.getClass().getMethod(methodName);
                Object result = method.invoke(obj);
                if (result instanceof Long) {
                    return (Long) result;
                }
            } catch (Exception ignored) {
                // Intentionally ignored as we're trying multiple method names
            }
        }
        return null;
    }
}