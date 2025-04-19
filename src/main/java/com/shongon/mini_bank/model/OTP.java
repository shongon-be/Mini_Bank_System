package com.shongon.mini_bank.model;

import com.shongon.mini_bank.constant.OtpStatus;
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

    @Enumerated(EnumType.STRING)
    OtpStatus status;

    LocalDateTime createdAt;
}
