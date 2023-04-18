package com.mastercard.intrabank.infra.inmemory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mastercard.intrabank.domain.model.exceptions.AccountNotFoundException;

@Service
public class InMemoryServiceImpl implements InMemoryService {
    final private Map<String, BigDecimal> accountBalance = new HashMap<>();

    public InMemoryServiceImpl() {
        addAccount("111", BigDecimal.valueOf(100.00));
        addAccount("112", BigDecimal.valueOf(200.00));
        addAccount("113", BigDecimal.valueOf(300.00));
        addAccount("114", BigDecimal.valueOf(400.00));
        addAccount("115", BigDecimal.valueOf(500.00));
    }

    @Override
    public void addAccount(String accountId, BigDecimal initialBalance) {
        accountBalance.put(accountId, initialBalance);
    }

    @Override
    public BigDecimal getAccountBalance(String accountId) {
        if (accountBalance.containsKey(accountId)) {
            return accountBalance.get(accountId);
        } else {
            throw new AccountNotFoundException("Account not found.");
        }
    }

    @Override
    public Map<String, BigDecimal> updateAccountBalance(String accountId, BigDecimal newBalance) {
        if (accountBalance.containsKey(accountId)) {
            accountBalance.put(accountId, newBalance);
            return accountBalance;
        } else {
            throw new AccountNotFoundException("Account not found.");
        }
    }

    @Override
    public boolean accountExists(String accountId) {
        return accountBalance.containsKey(accountId);
    }

}
