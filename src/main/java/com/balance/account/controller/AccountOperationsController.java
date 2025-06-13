package com.balance.account.controller;

import com.balance.account.entity.AccountTransaction;
import com.balance.account.entity.Balance;
import com.balance.account.model.AccountTransactionDto;
import com.balance.account.model.Currency;
import com.balance.account.model.ExchangeRate;
import com.balance.account.model.TransactionType;
import com.balance.account.service.AccountTransactionService;
import com.balance.account.service.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
@Slf4j
public class AccountOperationsController {

    private final BalanceService balanceService;
    private final AccountTransactionService accountTransactionService;



    @PostMapping("/add-transaction")
    public ResponseEntity<?> addTransaction(AccountTransactionDto accountTransactionDto){
        if (accountTransactionDto == null){
            return ResponseEntity.ok("Данные о проводимой транзакции отсутствуют");
        }
        if (!StringUtils.hasText(accountTransactionDto.getNameBalance())){
            return ResponseEntity.ok("Данные о имени баланса отсутствуют");
        }
        if (accountTransactionDto.getType() == null){
            return ResponseEntity.ok("Данные о типе операции отсутствуют");
        }
        if (accountTransactionDto.getAmount() == null){
            return ResponseEntity.ok("Данные о сумме операции отсутствуют");
        }
        if (accountTransactionDto.getAmount() <= 0){
            return ResponseEntity.ok("Сумма операции должна быть больше 0");
        }

        Double amountAccountTransaction = resolveCurrency(accountTransactionDto);
        if (amountAccountTransaction == null){
            return ResponseEntity.ok("Нет такой валюты для расчетов");
        }

        Optional<Balance> balance = balanceService.findByName(accountTransactionDto.getNameBalance());
        if (balance.isEmpty()){
            Balance newBalance = new Balance();
            newBalance.setName(accountTransactionDto.getNameBalance());
            newBalance.setCurrency(Currency.USD);
            newBalance.setAmountBalance(0.0D);

            balance = Optional.of(balanceService.save(newBalance));
        }

        if (TransactionType.WITHDRAWAL == accountTransactionDto.getType()){
            if (balance.get().getAmountBalance() < amountAccountTransaction){
                return ResponseEntity.ok("Сумма запрошенных средств превышает сумму баланса");
            }
        }
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setType(accountTransactionDto.getType());
        accountTransaction.setAmount(accountTransactionDto.getAmount());
        accountTransaction.setCurrency(accountTransactionDto.getCurrency());
        accountTransaction.setTimestampTransaction(OffsetDateTime.now());
        accountTransaction.setBalance(balance.get());

        AccountTransaction result = accountTransactionService.save(accountTransaction);
        log.info("Создана операция {} по балансу {}", result.getId(), balance.get().getName());

        balance.get().setAmountBalance(
                TransactionType.DEPOSIT == accountTransactionDto.getType() ?
                balance.get().getAmountBalance() + amountAccountTransaction :
                        balance.get().getAmountBalance() - amountAccountTransaction
                );

        Balance updateBalance = balanceService.save(balance.get());
        log.info("Обновлена сумма {} баланса {}", updateBalance.getAmountBalance(), updateBalance.getName());

        return ResponseEntity.ok("Транзакция проведена");
    }

    @GetMapping("/all_transaction/{name}")
    public List<AccountTransactionDto> findAllByNameBalance(@PathVariable String name){
        if (!StringUtils.hasText(name)){
            throw new RuntimeException("Нет данных о имени баланса");
        }
        List<AccountTransaction> result = accountTransactionService.findAllByBalanceName(name);
        if (CollectionUtils.isEmpty(result)){
            return  Collections.emptyList();
        }
        return result.stream()
                .map(a -> {
                    AccountTransactionDto dto = new AccountTransactionDto();
                    dto.setId(a.getId());
                    dto.setAmount(a.getAmount());
                    dto.setCurrency(a.getCurrency());
                    dto.setType(a.getType());
                    dto.setTimestampTransaction(a.getTimestampTransaction());
                    dto.setNameBalance(name);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/sum-amount-balance/{name}")
    public ResponseEntity<?> getBalanceByName(@PathVariable String name){
        if (!StringUtils.hasText(name)){
            throw new RuntimeException("Нет данных о имени баланса");
        }
        Optional<Balance> result = balanceService.findByName(name);
        if (result.isEmpty()){
            return ResponseEntity.ok(String.format("Нет баланса по данному имени %s", name));
        }
        return ResponseEntity.ok(result.get());
    }

    @Nullable
    private Double resolveCurrency(AccountTransactionDto accountTransactionDto) {
        switch (accountTransactionDto.getCurrency()){
            case USD -> {
                return accountTransactionDto.getAmount();
            }
            case EUR,BYN,RUB -> {
               Double coefficient = ExchangeRate.valueOf(accountTransactionDto.getCurrency().name()).getCoefficient();
               return accountTransactionDto.getAmount() * coefficient;
            }
            default -> {
                return null;
            }
        }
    }
}
