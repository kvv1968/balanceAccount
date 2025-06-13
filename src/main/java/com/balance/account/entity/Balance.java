package com.balance.account.entity;

import com.balance.account.model.Currency;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "balance")
@Entity
@NoArgsConstructor
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true)
    String name;

    @Column
    Double amountBalance;

    @Enumerated(EnumType.STRING)
    @Column
    Currency currency;

}
