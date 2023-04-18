package com.mastercard.intrabank.domain.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)

public class UnsuccessfulTransactionException extends RuntimeException {
    public UnsuccessfulTransactionException(final String message) {
        super(message);
    }

}
