package com.shongon.mini_bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
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

    String password;

    @Email
    String email;

    String phoneNumber;

    String status;

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

enum UserStatus {
    ACTIVE, SUSPENDED, DELETED
}
