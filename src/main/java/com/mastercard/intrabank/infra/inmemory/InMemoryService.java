package com.mastercard.intrabank.infra.inmemory;

import java.math.BigDecimal;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

public interface InMemoryService {
    void addAccount(String accountId, BigDecimal initialBalance);

    BigDecimal getAccountBalance(String accountId) throws AccountNotFoundException;

    Map<String, BigDecimal> updateAccountBalance(String accountId, BigDecimal newBalance) throws AccountNotFoundException;

    boolean accountExists(String accountId);

}
