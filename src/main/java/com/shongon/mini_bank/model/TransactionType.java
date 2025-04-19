package com.shongon.mini_bank.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long transactionTypeId;

    String transactionTypeName;

    @OneToMany(mappedBy = "transactionType")
    List<Transaction> transactions;
}
