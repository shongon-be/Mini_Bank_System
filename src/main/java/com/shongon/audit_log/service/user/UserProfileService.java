package com.shongon.audit_log.service.user;

import com.shongon.audit_log.dto.request.role.AssignNRemoveRoleRequest;
import com.shongon.audit_log.dto.request.user.ChangePasswordRequest;
import com.shongon.audit_log.dto.response.user.ChangePasswordResponse;
import com.shongon.audit_log.dto.response.user.ViewUserProfileResponse;
import com.shongon.audit_log.exception.AppException;
import com.shongon.audit_log.exception.ErrorCode;
import com.shongon.audit_log.mapper.UserMapper;
import com.shongon.audit_log.model.Role;
import com.shongon.audit_log.model.User;
import com.shongon.audit_log.repository.RoleRepository;
import com.shongon.audit_log.repository.UserRepository;
import com.shongon.audit_log.utils.audit.Auditable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    // USER
    @Transactional(readOnly = true)
    @Auditable(action = "GET_MY_INFO", entityType = "USER")
    @PostAuthorize("returnObject.username == authentication.name")
    public ViewUserProfileResponse getMyInfo() {
        User info = getCurrentAuthenticatedUser();
        return userMapper.toViewUserProfileResponse(info);
    }

    @Transactional
    @Auditable(action = "CHANGE_PASSWORD", entityType = "USER")
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        User curUser = getCurrentAuthenticatedUser();

        // Kiểm tra xác thực thủ công bên trong phương thức
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!curUser.getUsername().equals(currentUsername)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION);
        }

        userMapper.changePassword(curUser, request);

        if (!passwordEncoder.matches(request.getOldPassword(), curUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }

        curUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return userMapper.toChangePasswordResponse(userRepository.save(curUser));
    }

    // ADMIN
    @Transactional
    @Auditable(action = "ASSIGN_ROLE_TO_USER", entityType = "USER")
    public void assignRoleToUser(Long userId, AssignNRemoveRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Role role = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Transactional
    @Auditable(action = "REMOVE_ROLE_FROM_USER", entityType = "USER")
    public void removeRoleFromUser(Long userId, AssignNRemoveRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Role role = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Set<Role> roles = user.getRoles();
        roles.remove(role);
        user.setRoles(roles);

        userRepository.save(user);
    }

    private User getCurrentAuthenticatedUser() {
        var context = SecurityContextHolder.getContext();
        String whoIsLogged = context.getAuthentication().getName();

        return userRepository
                .findByUsername(whoIsLogged)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
