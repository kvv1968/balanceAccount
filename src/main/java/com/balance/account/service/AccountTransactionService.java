package com.balance.account.service;

import com.balance.account.entity.AccountTransaction;
import com.balance.account.repo.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountTransactionService {

    private final AccountTransactionRepository accountTransactionRepository;

    @Transactional
    public AccountTransaction save(AccountTransaction accountTransaction){
        return accountTransactionRepository.save(accountTransaction);
    }

    @Transactional(readOnly = true)
    public List<AccountTransaction> findAllByBalanceName(String name){
        return accountTransactionRepository.findAllByBalance_Name(name);
    }
}
