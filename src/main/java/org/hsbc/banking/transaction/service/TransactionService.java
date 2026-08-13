package org.hsbc.banking.transaction.service;

import org.hsbc.banking.account.entity.Account;
import org.hsbc.banking.account.repository.AccountRepository;
import org.hsbc.banking.exception.ResourceNotFoundException;
import org.hsbc.banking.transaction.dto.TransactionRequest;
import org.hsbc.banking.transaction.dto.TransferRequest;
import org.hsbc.banking.transaction.entity.Transaction;
import org.hsbc.banking.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Transaction deposit(Long accountId, TransactionRequest request) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId
                        )
                );

        if (request.getAmount() == null ||
                request.getAmount().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be greater than zero");
        }

        account.setBalance(
                account.getBalance().add(request.getAmount())
        );

        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(Long accountId, TransactionRequest request) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId
                        )
                );

        if (request.getAmount() == null ||
                request.getAmount().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient account balance");
        }

        account.setBalance(
                account.getBalance().subtract(request.getAmount())
        );

        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public void transfer(TransferRequest request) {

        Long fromAccountId = request.getFromAccountId();
        Long toAccountId = request.getToAccountId();
        BigDecimal amount = request.getAmount();

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must be different");
        }

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero");
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Source account not found with id: "
                                        + fromAccountId));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Destination account not found with id: "
                                        + toAccountId));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient account balance");
        }

        fromAccount.setBalance(
                fromAccount.getBalance().subtract(amount)
        );

        toAccount.setBalance(
                toAccount.getBalance().add(amount)
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(amount);
        debitTransaction.setTransactionType("TRANSFER_DEBIT");
        debitTransaction.setTransactionDate(LocalDateTime.now());
        debitTransaction.setAccount(fromAccount);

        transactionRepository.save(debitTransaction);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAmount(amount);
        creditTransaction.setTransactionType("TRANSFER_CREDIT");
        creditTransaction.setTransactionDate(LocalDateTime.now());
        creditTransaction.setAccount(toAccount);

        transactionRepository.save(creditTransaction);
    }
}