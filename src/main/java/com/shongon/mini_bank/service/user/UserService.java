package com.shongon.mini_bank.service.user;

import com.shongon.mini_bank.constant.PredefinedRole;
import com.shongon.mini_bank.constant.status.UserStatus;
import com.shongon.mini_bank.dto.request.user.RegisterRequest;
import com.shongon.mini_bank.dto.request.user.UpdateUserInfoRequest;
import com.shongon.mini_bank.dto.response.user.RegisterResponse;
import com.shongon.mini_bank.dto.response.user.UpdateUserInfoResponse;
import com.shongon.mini_bank.dto.response.user.ViewAllUsersResponse;
import com.shongon.mini_bank.dto.response.user.ViewUserProfileResponse;
import com.shongon.mini_bank.exception.AppException;
import com.shongon.mini_bank.exception.ErrorCode;
import com.shongon.mini_bank.mapper.UserMapper;
import com.shongon.mini_bank.model.Role;
import com.shongon.mini_bank.model.User;
import com.shongon.mini_bank.repository.AuditLogRepository;
import com.shongon.mini_bank.repository.RoleRepository;
import com.shongon.mini_bank.repository.UserRepository;
import com.shongon.mini_bank.service.security.AuditLogService;
import com.shongon.mini_bank.utils.audit.Auditable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    AuditLogRepository auditLogRepository;
    AuditLogService auditLogService;

    @Transactional
    @Auditable(action = "REGISTER", entityType = "USER")
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.registerUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findByRoleName(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        // Lưu user trước
        User savedUser = userRepository.save(user);

        // Sử dụng phương thức mới để log theo username
        auditLogService.logRegisterAction(
                savedUser.getUsername(),
                "REGISTER",
                "USER",
                savedUser.getUserId(),
                "New register: " + savedUser.getUsername()
        );

        return userMapper.toRegisterResponse(savedUser);
    }

    @Transactional
    @Auditable(action = "UPDATE_USER_INFO", entityType = "USER")
    @PreAuthorize("@userRepository.findById(#userId).get().username == authentication.name")
    public UpdateUserInfoResponse updateUserInfo(Long userId, UpdateUserInfoRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);

        return userMapper.toUpdateUserInfoResponse(userRepository.save(user));
    }

    // ADMIN FUNCTION: SOFT_DELETE
    @Transactional
    @Auditable(action = "LOCK_USER", entityType = "USER")
    public void lockUser(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(status);

        if (status == UserStatus.SUSPENDED) {
            user.setDeletedAt(LocalDateTime.now());
        } else {
            user.setDeletedAt(null);
        }

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Auditable(action = "VIEW_USER_PROFILE", entityType = "USER")
    public ViewUserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toViewUserProfileResponse(user);
    }

    @Transactional(readOnly = true)
    @Auditable(action = "VIEW_ALL_USERS", entityType = "USER")
    public List<ViewAllUsersResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toViewAllUsersResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Auditable(action = "DELETE_USER", entityType = "USER")
    public void deleteUser(Long userId) {
        // Find the user to ensure they exist (optional but good practice)
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Delete all associated audit logs
        auditLogRepository.deleteByUser_UserId(userId);

        // Now delete the user
        userRepository.delete(userToDelete);
    }
}
