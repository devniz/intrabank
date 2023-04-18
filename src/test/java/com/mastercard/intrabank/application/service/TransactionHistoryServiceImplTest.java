package com.mastercard.intrabank.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mastercard.intrabank.domain.model.MiniStatement;

class TransactionHistoryServiceImplTest {

    @Test
    @DisplayName("Verify save statement")
    public void givenMiniStatement_WhenSaveStatementIsCalled_ThenSaveIt() {
        TransactionHistoryServiceImpl transactionHistoryService = new TransactionHistoryServiceImpl();
        transactionHistoryService.saveStatement(buildMiniStatement());

        List<MiniStatement> miniStatement = transactionHistoryService.getMiniStatement("115");

        assertEquals(1, miniStatement.size());
        assertEquals("115", miniStatement.get(0).getAccountId());
        assertEquals("DEBIT", miniStatement.get(0).getTransactionType());
        assertEquals(BigDecimal.valueOf(25.00), miniStatement.get(0).getAmount());
        assertEquals("GBP", miniStatement.get(0).getCurrency());
    }

    public MiniStatement buildMiniStatement() {
        return MiniStatement.builder()
                .accountId("115")
                .transactionDate(LocalDateTime.now())
                .transactionType("DEBIT")
                .amount(BigDecimal.valueOf(25.00))
                .currency("GBP")
                .build();
    }
}