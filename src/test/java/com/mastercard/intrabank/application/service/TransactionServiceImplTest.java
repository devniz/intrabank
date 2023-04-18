package com.mastercard.intrabank.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mastercard.intrabank.domain.model.TransactionRequest;
import com.mastercard.intrabank.domain.model.TransactionResponse;
import com.mastercard.intrabank.domain.model.exceptions.UnsuccessfulTransactionException;
import com.mastercard.intrabank.infra.inmemory.InMemoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    @InjectMocks
    TransactionServiceImpl service;
    @Mock
    TransactionServiceImpl mockTransactionService;
    @Mock
    private InMemoryServiceImpl mockMemoryService;

    @Test
    @DisplayName("Verify invalid account sending funds")
    public void givenInvalidAccount_WhenSendIsCalled_ThenItShouldThrowUnsuccessfulTransactionException() {
        // given
        when(mockMemoryService.accountExists("999")).thenReturn(false);

        // when
        var transactionRequest = buildTransactionRequestForUnknownAccount();

        // then
        assertThrows(UnsuccessfulTransactionException.class, () -> {
            service.send(transactionRequest);
        });
    }

    @Test
    @DisplayName("Verify account with not enough funds")
    public void givenAccountWithNotEnoughFunds_WhenSendIsCalled_ThenItShouldThrowUnsuccessfulTransactionException() {
        // given
        when(mockMemoryService.accountExists("111")).thenReturn(true);
        when(mockMemoryService.accountExists("112")).thenReturn(true);
        when(mockMemoryService.getAccountBalance("111")).thenReturn(BigDecimal.valueOf(5.25));

        // when
        var transactionRequest = buildTransactionRequest();

        assertThrows(UnsuccessfulTransactionException.class, () -> {
            service.send(transactionRequest);
        });
    }

    @Test
    @DisplayName("Verify sending funds")
    public void givenAccountWithEnoughFunds_WhenSendIsCalled_ThenItShouldReturnTransactionResponse() {
        // given
        when(mockTransactionService.send(buildTransactionRequest())).thenReturn(buildtransactionResponse());

        // when
        var expected = mockTransactionService.send(buildTransactionRequest());

        // then
        assertEquals(expected.getFromAccount(), "111");
        assertEquals(expected.getToAccount(), "112");
        assertEquals(expected.getAmount(), BigDecimal.valueOf(100.00));
        assertEquals(expected.getSenderNewBalance(), BigDecimal.valueOf(0.00));
        assertEquals(expected.getReceiverNewBalance(), BigDecimal.valueOf(300.00));
    }

    TransactionRequest buildTransactionRequest() {
        return TransactionRequest.builder()
                .senderAccountId("111")
                .receiverAccountId("112")
                .amount(BigDecimal.valueOf(100.00))
                .build();
    }

    TransactionRequest buildTransactionRequestForUnknownAccount() {
        return TransactionRequest.builder()
                .senderAccountId("999")
                .receiverAccountId("112")
                .amount(BigDecimal.valueOf(5.00))
                .build();
    }

    TransactionResponse buildtransactionResponse() {
        return TransactionResponse.builder()
                .fromAccount("111")
                .toAccount("112")
                .currency("GBP")
                .transactionDate(LocalDateTime.now())
                .amount(BigDecimal.valueOf(100.00))
                .senderNewBalance(BigDecimal.valueOf(0.00))
                .receiverNewBalance(BigDecimal.valueOf(300.00))
                .build();
    }
}