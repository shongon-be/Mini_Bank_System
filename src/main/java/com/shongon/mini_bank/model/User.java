package com.shongon.mini_bank.model;

import com.shongon.mini_bank.constant.status.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    String username;

    String fullname;

    String password;

    String email;

    String phoneNumber;

    LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    UserStatus status;

    @ManyToMany
    Set<Role> roles;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(insertable = false)
    LocalDateTime updatedAt;

    LocalDateTime lockedAt;
}
