package com.balance.account.repo;

import com.balance.account.entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction,String> {

    List<AccountTransaction> findAllByBalance_Name(String name);

}
