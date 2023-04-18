package com.mastercard.intrabank.domain.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TransactionRequest {
    final private String senderAccountId;

    final private String receiverAccountId;

    final private BigDecimal amount;
}
