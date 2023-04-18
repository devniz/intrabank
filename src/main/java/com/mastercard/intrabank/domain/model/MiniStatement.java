package com.mastercard.intrabank.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MiniStatement {
    final private String accountId;

    final private BigDecimal amount;

    final private String currency;

    final String transactionType;

    final LocalDateTime transactionDate;

}
