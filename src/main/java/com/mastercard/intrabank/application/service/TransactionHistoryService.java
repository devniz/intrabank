package com.mastercard.intrabank.application.service;

import java.util.List;

import com.mastercard.intrabank.domain.model.MiniStatement;

public interface TransactionHistoryService {
    void saveStatement(MiniStatement statement);

    List<MiniStatement> getMiniStatement(String accountId);
}
