package com.mastercard.intrabank.application.service;

import java.util.List;

import com.mastercard.intrabank.domain.model.AccountBalanceResponse;
import com.mastercard.intrabank.domain.model.MiniStatement;

public interface AccountService {
    AccountBalanceResponse getCurrentAccountBalance(String accountId);

    List<MiniStatement> getMiniStatement(String accountId);
}
