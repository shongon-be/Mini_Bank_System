package com.shongon.mini_bank.model;

import com.shongon.mini_bank.constant.status.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "bank_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3-50 characters")
    String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character")
    String password;

    @NotBlank(message = "Email cannot be empty")
    @Email
    String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number must be at least 10 digits")
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    UserStatus status;

    @ManyToMany
    Set<Role> roles;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    LocalDateTime updatedAt;

    LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade =CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKey(name = "accountNumber")
    Map<String, Account> accountMap;
}
