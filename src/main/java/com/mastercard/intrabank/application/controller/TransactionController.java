package com.mastercard.intrabank.application.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.intrabank.domain.model.TransactionRequest;
import com.mastercard.intrabank.domain.model.TransactionResponse;
import com.mastercard.intrabank.application.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService service;

    /**
     * Endpoint to send funds to another existent account.
     * Return TransactionResponse or 404 when account not found.
     * Return 401 if account does not have enough funds.
     */
    @PostMapping(path = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> send(@RequestBody final TransactionRequest request) throws Exception {
        return ResponseEntity.ok().body(service.send(request));
    }
}
