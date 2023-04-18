package com.mastercard.intrabank.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TransactionResponse {
    final private String fromAccount;

    final private String toAccount;

    final private String currency;

    final private LocalDateTime transactionDate;

    final private BigDecimal amount;

    final private BigDecimal senderNewBalance;

    final private BigDecimal receiverNewBalance;
}


