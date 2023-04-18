package com.mastercard.intrabank.domain.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InsufficientFundException extends RuntimeException {
    public InsufficientFundException(final String message) {
        super(message);
    }
}
