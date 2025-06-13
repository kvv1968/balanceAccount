package com.balance.account.service;

import com.balance.account.entity.Balance;
import com.balance.account.repo.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    @Transactional(readOnly = true)
    public Optional<Balance> findByName(String nameBalance){
        if (!StringUtils.hasText(nameBalance)){
            throw new RuntimeException("Отсуствует имя баланса");
        }
        return balanceRepository.findByName(nameBalance);
    }

    @Transactional
    public Balance save(Balance balance) {
        return balanceRepository.save(balance);
    }
}
