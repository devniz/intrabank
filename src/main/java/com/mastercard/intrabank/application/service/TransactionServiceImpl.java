package com.mastercard.intrabank.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mastercard.intrabank.domain.model.MiniStatement;
import com.mastercard.intrabank.domain.model.TransactionRequest;
import com.mastercard.intrabank.domain.model.TransactionResponse;
import com.mastercard.intrabank.domain.model.exceptions.AccountNotFoundException;
import com.mastercard.intrabank.domain.model.exceptions.InsufficientFundException;
import com.mastercard.intrabank.domain.model.exceptions.UnsuccessfulTransactionException;
import com.mastercard.intrabank.infra.inmemory.InMemoryServiceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    final private TransactionHistoryService historyService;

    final private InMemoryServiceImpl memoryService;

    @Override
    public TransactionResponse send(TransactionRequest request) {
        String senderAccountId = request.getSenderAccountId();
        String receiverAccountId = request.getReceiverAccountId();
        BigDecimal amountToSend = request.getAmount();

        try {
            // Check if sender account exists
            checkIfAccountExists(senderAccountId);

            // Check if receiver account exists
            checkIfAccountExists(receiverAccountId);

            // Check if sender has sufficient funds
            checkIfSenderHasEnoughFunds(senderAccountId, amountToSend);

            // Perform funds transfer
            TransactionResponse transactionResponse = doTransfer(senderAccountId, receiverAccountId, amountToSend);

            // If everything went fine, we save the statement for each account.
            saveTransaction(senderAccountId, amountToSend, "DEBIT", transactionResponse.getTransactionDate());
            saveTransaction(receiverAccountId, amountToSend, "CREDIT", transactionResponse.getTransactionDate());
            return transactionResponse;
        } catch (Exception ex) {
            log.error("Transaction failed for this reason: {}", ex.getMessage());
            throw new UnsuccessfulTransactionException(ex.getMessage());
        }
    }

    private void checkIfAccountExists(String accountId) {
        if (!memoryService.accountExists(accountId)) {
            log.error("Account not found during transaction, accountId is {}", accountId);
            throw new AccountNotFoundException("Account not found.");
        }
    }

    private void checkIfSenderHasEnoughFunds(String senderAccountId, BigDecimal amountToSend) {
        BigDecimal senderAccountBalance = memoryService.getAccountBalance(senderAccountId);

        if (senderAccountBalance.compareTo(amountToSend) < 0) {
            log.error("Send account does not have enough fund. Want to send an amount of {}, but has {} available ", amountToSend, senderAccountBalance);
            throw new InsufficientFundException("Sender does not have enough fund to perform this transaction.");
        }
    }

    private synchronized TransactionResponse doTransfer(String senderAccountId, String receiverAccountId, BigDecimal amountToSend) {
        BigDecimal senderAccountBalance = memoryService.getAccountBalance(senderAccountId);
        BigDecimal receiverAccountBalance = memoryService.getAccountBalance(receiverAccountId);
        BigDecimal senderNewBalance = senderAccountBalance.subtract(amountToSend);
        BigDecimal receiverNewBalance = receiverAccountBalance.add(amountToSend);
        LocalDateTime now = LocalDateTime.now();

        memoryService.updateAccountBalance(senderAccountId, senderNewBalance);
        memoryService.updateAccountBalance(receiverAccountId, receiverNewBalance);

        return TransactionResponse.builder()
                .fromAccount(senderAccountId)
                .toAccount(receiverAccountId)
                .currency("GPB")
                .transactionDate(now)
                .amount(amountToSend)
                .senderNewBalance(senderNewBalance)
                .receiverNewBalance(receiverNewBalance)
                .build();
    }

    private void saveTransaction(String accountId, BigDecimal amount, String transactionType, LocalDateTime transactionDate) {
        MiniStatement currentStm = MiniStatement.builder()
                .accountId(accountId)
                .amount(amount)
                .currency("GBP")
                .transactionType(transactionType)
                .transactionDate(transactionDate)
                .build();

        historyService.saveStatement(currentStm);
    }

}
