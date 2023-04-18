package com.mastercard.intrabank.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mastercard.intrabank.domain.model.AccountBalanceResponse;
import com.mastercard.intrabank.domain.model.MiniStatement;
import com.mastercard.intrabank.domain.model.exceptions.AccountNotFoundException;
import com.mastercard.intrabank.infra.inmemory.InMemoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private InMemoryServiceImpl mockMemoryService;
    @Mock
    private TransactionHistoryService mockHistoryService;

    @Test
    @DisplayName("Verify get balance")
    public void givenValidAccountWithSomeBalance_WhenGetCurrentAccountBalanceIsCalled_ThenShouldReturnBalance() {
        // given
        when(mockMemoryService.getAccountBalance("111")).thenReturn(BigDecimal.valueOf(100.00));

        // when
        AccountBalanceResponse result = accountService.getCurrentAccountBalance("111");

        // then
        assertEquals("111", result.getAccountId());
        assertEquals(BigDecimal.valueOf(100.00), result.getBalance());
        assertEquals("GBP", result.getCurrency());
    }

    @Test
    @DisplayName("Verify throw AccountNotFoundException")
    public void givenInvalidAccount_WhenGetCurrentAccountBalanceIsCalled_ThenShouldThrowAccountNotFoundException() {
        // given
        when(mockMemoryService.getAccountBalance("999")).thenThrow(new AccountNotFoundException("Account not found"));

        // then
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getCurrentAccountBalance("999");
        });
    }

    @Test
    @DisplayName("Verify get mini statements")
    public void givenOneTransactionForAccount111_WhenGetMiniStatement_ThenShouldReturnStatement() {
        // given
        var expectedStatement = buildMiniStatement();
        when(mockHistoryService.getMiniStatement("111")).thenReturn(expectedStatement);

        // when
        List<MiniStatement> actualStatement = accountService.getMiniStatement("111");

        // then
        Assertions.assertThat(actualStatement)
                .usingRecursiveComparison()
                .isEqualTo(expectedStatement);

        verify(mockHistoryService, times(1)).getMiniStatement("111");
    }

    private List<MiniStatement> buildMiniStatement() {
        return List.of(MiniStatement.builder()
                .accountId("111")
                .transactionDate(LocalDateTime.now())
                .transactionType("DEBIT")
                .currency("GBP")
                .amount(BigDecimal.valueOf(10.00))
                .build());
    }

}