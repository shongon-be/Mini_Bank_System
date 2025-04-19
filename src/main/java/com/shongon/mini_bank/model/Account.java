package com.shongon.mini_bank.model;

import com.shongon.mini_bank.constant.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountId;

    String accountNumber;

    Double balance;

    @Enumerated(EnumType.STRING)
    AccountStatus status;

    String currency;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}


