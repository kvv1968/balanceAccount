package com.balance.account.entity;

import com.balance.account.model.Currency;
import com.balance.account.model.TransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "account_transaction")
@Entity
@NoArgsConstructor
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Enumerated(EnumType.STRING)
    @Column
    TransactionType type;

    @Column
    Double amount;

    @Enumerated(EnumType.STRING)
    @Column
    Currency currency;

    @Column
    OffsetDateTime timestampTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balance_id")
    Balance balance;

}
