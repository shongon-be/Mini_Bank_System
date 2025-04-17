package com.shongon.mini_bank.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long otpId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String code;
    String purpose;
    LocalDateTime expiryDate;
    String status;
    LocalDateTime createdAt;
}
