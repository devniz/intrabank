package com.mastercard.intrabank.application.service;

import com.mastercard.intrabank.domain.model.TransactionRequest;
import com.mastercard.intrabank.domain.model.TransactionResponse;

public interface TransactionService {
    TransactionResponse send(TransactionRequest request);
}
