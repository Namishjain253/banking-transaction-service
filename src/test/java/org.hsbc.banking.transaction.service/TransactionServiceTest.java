package org.hsbc.banking.transaction.service;

import org.hsbc.banking.account.entity.Account;
import org.hsbc.banking.account.repository.AccountRepository;
import org.hsbc.banking.transaction.dto.TransactionRequest;
import org.hsbc.banking.transaction.dto.TransferRequest;
import org.hsbc.banking.transaction.entity.Transaction;
import org.hsbc.banking.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void deposit_shouldIncreaseAccountBalance() {

        Account account = new Account();
        account.setBalance(new BigDecimal("4200"));

        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("500"));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(account));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result =
                transactionService.deposit(2L, request);

        assertEquals(
                new BigDecimal("4700"),
                account.getBalance()
        );

        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void withdraw_shouldDecreaseAccountBalance() {

        Account account = new Account();
        account.setBalance(new BigDecimal("4200"));

        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("1000"));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(account));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result =
                transactionService.withdraw(2L, request);

        assertEquals(
                new BigDecimal("3200"),
                account.getBalance()
        );

        verify(accountRepository).save(account);
        verify(transactionRepository)
                .save(any(Transaction.class));
    }

    @Test
    void withdraw_shouldThrowExceptionWhenBalanceIsInsufficient() {

        Account account = new Account();
        account.setBalance(new BigDecimal("4200"));

        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("5000"));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(account));

        assertThrows(
                IllegalArgumentException.class,
                () -> transactionService.withdraw(2L, request)
        );

        verify(accountRepository, never()).save(account);
        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }

    @Test
    void transfer_shouldMoveMoneyBetweenAccounts() {

        Account fromAccount = new Account();
        fromAccount.setBalance(new BigDecimal("5000"));

        Account toAccount = new Account();
        toAccount.setBalance(new BigDecimal("3000"));

        TransferRequest request = new TransferRequest();

        // Use the setters available in your TransferRequest
        request.setFromAccountId(2L);
        request.setToAccountId(3L);
        request.setAmount(new BigDecimal("1000"));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(3L))
                .thenReturn(Optional.of(toAccount));

        transactionService.transfer(request);

        assertEquals(
                new BigDecimal("4000"),
                fromAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("4000"),
                toAccount.getBalance()
        );

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }

    @Test
    void transfer_shouldFailWhenSourceBalanceIsInsufficient() {

        Account fromAccount = new Account();
        fromAccount.setBalance(new BigDecimal("500"));

        Account toAccount = new Account();
        toAccount.setBalance(new BigDecimal("3000"));

        TransferRequest request = new TransferRequest();
        request.setFromAccountId(2L);
        request.setToAccountId(3L);
        request.setAmount(new BigDecimal("1000"));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(3L))
                .thenReturn(Optional.of(toAccount));

        assertThrows(
                IllegalArgumentException.class,
                () -> transactionService.transfer(request)
        );

        assertEquals(
                new BigDecimal("500"),
                fromAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("3000"),
                toAccount.getBalance()
        );

        verify(accountRepository, never()).save(any(Account.class));
    }

}