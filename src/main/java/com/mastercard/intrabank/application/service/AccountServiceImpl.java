package com.mastercard.intrabank.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mastercard.intrabank.domain.model.AccountBalanceResponse;
import com.mastercard.intrabank.domain.model.MiniStatement;
import com.mastercard.intrabank.infra.inmemory.InMemoryServiceImpl;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    final private InMemoryServiceImpl memoryService;

    final private TransactionHistoryService historyService;

    @Override
    public AccountBalanceResponse getCurrentAccountBalance(String accountId) {
        return AccountBalanceResponse.builder()
                .accountId(accountId)
                .balance(memoryService.getAccountBalance(accountId))
                .currency("GBP")
                .build();
    }

    @Override
    public List<MiniStatement> getMiniStatement(String accountId) {
        return historyService.getMiniStatement(accountId);
    }

}
