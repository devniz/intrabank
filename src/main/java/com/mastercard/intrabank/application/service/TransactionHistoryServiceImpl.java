package com.mastercard.intrabank.application.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mastercard.intrabank.domain.model.MiniStatement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final List<Map<String, MiniStatement>> transactionResponseList = new ArrayList<>();

    @Override
    public void saveStatement(MiniStatement statement) {
        HashMap<String, MiniStatement> transactionByAccountId = new HashMap<>();

        transactionByAccountId.put(statement.getAccountId(), statement);
        transactionResponseList.add(transactionByAccountId);
    }

    @Override
    public List<MiniStatement> getMiniStatement(String accountId) {
        return transactionResponseList.stream()
                .filter(transactionResponseMap -> transactionResponseMap.containsKey(accountId))
                .map(transactionResponseMap -> transactionResponseMap.get(accountId))
                .collect(Collectors.toList());
    }
}
