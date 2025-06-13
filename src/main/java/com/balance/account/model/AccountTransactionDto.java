package com.balance.account.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountTransactionDto {
    /**
     * id транзакции
     */
    String id;

    /**
     * имя баланса уникальное
     */
    String nameBalance;

    /**
     * Тип транзакции
     *   DEPOSIT, WITHDRAWAL
     */
    TransactionType type;

    /**
     * Сумма операции
     */
    Double amount;

    /**
     * Валюта операции
     */
    Currency currency;

    /**
     * временная метка: дата и время транзакции
     */
    OffsetDateTime timestampTransaction;

}
