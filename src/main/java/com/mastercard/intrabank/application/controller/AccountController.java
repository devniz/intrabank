package com.mastercard.intrabank.application.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mastercard.intrabank.domain.model.AccountBalanceResponse;
import com.mastercard.intrabank.application.service.AccountService;
import com.mastercard.intrabank.domain.model.MiniStatement;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;

    /**
     * Endpoint to get Account balance
     * Return AccountBalanceResponse object or 404 when no account found.
     */
    @GetMapping(path = "/{accountId}/balance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(@PathVariable final String accountId) {
        return ResponseEntity.ok().body(service.getCurrentAccountBalance(accountId));
    }

    /**
     * Endpoint to get the mini statement
     * return MiniStatement object or 404 when no account found.
     */
    @GetMapping(path = "/{accountId}/statements/mini", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MiniStatement>> getMiniStatement(@PathVariable final String accountId) {
        return ResponseEntity.ok().body(service.getMiniStatement(accountId));
    }

}
