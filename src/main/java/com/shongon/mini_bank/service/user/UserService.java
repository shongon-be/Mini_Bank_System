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
import com.shongon.mini_bank.repository.RoleRepository;
import com.shongon.mini_bank.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
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

        return userMapper.toRegisterResponse(userRepository.save(user));
    }

    @Transactional
    public UpdateUserInfoResponse updateUserInfo(String username, UpdateUserInfoRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        return userMapper.toUpdateUserInfoResponse(userRepository.save(user));
    }

    // ADMIN FUNCTION: SOFT_DELETE
    @Transactional
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
    public ViewUserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toViewUserProfileResponse(user);
    }

    @Transactional(readOnly = true)
    public List<ViewAllUsersResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toViewAllUsersResponse)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) throw new AppException(ErrorCode.USER_NOT_FOUND);

        userRepository.deleteById(userId);
    }
}
