package com.mastercard.intrabank.domain.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AccountBalanceResponse {
    final private String accountId;

    final private BigDecimal balance;

    final private String currency;

}
