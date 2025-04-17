package com.shongon.mini_bank.model;

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
public class Transaction {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long transactionId;

    @ManyToOne
    @JoinColumn(name = "initiated_by_user_id")
    User initiatedBy;

    @ManyToOne
    @JoinColumn(name = "account_id_sender")
    Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "account_id_receiver")
    Account receiverAccount;

    Double amount;
    String type;

    @Enumerated(EnumType.STRING)
    TransactionStatus status;

    Double fee;
    String description;
    String currency;

    LocalDateTime createdAt;
    LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "transaction_type_id")
    TransactionType transactionType;
}
enum TransactionStatus {
    PENDING, COMPLETED, FAILED
}
