package org.hsbc.banking.transaction.service;

import org.hsbc.banking.account.entity.Account;
import org.hsbc.banking.account.repository.AccountRepository;
import org.hsbc.banking.exception.ResourceNotFoundException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        assertNotNull(result);
        assertEquals(new BigDecimal("500"), result.getAmount());
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
        assertNotNull(result);
        assertEquals(new BigDecimal("1000"), result.getAmount());
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

    @Test
    void createTransaction_shouldCreateAndSaveTransaction(){
        TransactionRequest request =  new TransactionRequest();
        request.setAmount(new BigDecimal("1000"));

        Transaction savedTransaction =  new Transaction();
        savedTransaction.setAmount(new BigDecimal("1000"));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        Transaction result = transactionService.createTransaction(request);

        assertNotNull(result);
        assertEquals(new BigDecimal("1000"),request.getAmount());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void getAllTransaction_shouldReturnAllTransactions(){
        Transaction transaction1 =  new Transaction();
        Transaction transaction2 =  new Transaction();

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transactions, result);

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void deleteTransaction_shouldDeleteExistingTransaction(){
        Long id = 1L;
        Transaction transaction =  new Transaction();
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(id);

        verify(transactionRepository, times(1)).findById(id);
        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    void deleteTransaction_shouldThrowExceptionWhenTransactionNotFound(){
        Long id = 999L;
        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> transactionService.deleteTransaction(id));

        assertEquals("Transaction not found with id: " + id, exception.getMessage());

        verify(transactionRepository, times(1)).findById(id);
        verify(transactionRepository,never()).delete(any(Transaction.class));
    }

    @Test
    void deposit_whenResourceNotFound_shouldThrowResourceNotFoundException(){
       Long accountId =1L;
       TransactionRequest request =  new TransactionRequest();
       request.setAmount(new BigDecimal("100"));

       when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

       assertThrows(ResourceNotFoundException.class,()-> transactionService.deposit(accountId,request));
    }

    @Test
    void deposit_whenAmountIsZero_shouldThrowIllegalArgumentException(){
        Long accountId =1L;
        TransactionRequest request =  new TransactionRequest();
        request.setAmount(BigDecimal.ZERO);

        Account account =  new Account();
        account.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class,()-> transactionService.deposit(accountId,request));
    }

    @Test
    void withdraw_whenAmountIsZero_shouldThrowIllegalArgumentException(){
        Long accountId =1L;
        TransactionRequest request =  new TransactionRequest();
        request.setAmount(BigDecimal.ZERO);
        Account account =  new Account();
        account.setBalance(BigDecimal.valueOf(1000));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        assertThrows(IllegalArgumentException.class,()-> transactionService.withdraw(accountId,request));
    }

    @Test
    void withdraw_whenResourceNotFound_shouldThrowResourceNotFoundException(){
        Long accountId =1L;
        TransactionRequest request =  new TransactionRequest();
        request.setAmount(new BigDecimal("100"));
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()-> transactionService.withdraw(accountId,request));
    }

    @Test
    void transfer_whenIllegalArgument_shouldThrowIllegalArgumentException(){
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccountId(1L);
        transferRequest.setToAccountId(1L);
        assertThrows(IllegalArgumentException.class,()-> transactionService.transfer(transferRequest));
    }

    @Test
    void transfer_whenAmountIllegalArgument_shouldThrowIllegalArgumentException(){
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromAccountId(1L);
        transferRequest.setAmount(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()-> transactionService.transfer(transferRequest));
        assertEquals("Transfer amount must be greater than zero", exception.getMessage());
    }

    @Test
    void transfer_ToAccountIdResourceNotFound_shouldThrowResourceNotFoundException(){
        Long fromId = 1L;
        Long toId = 2L;
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(fromId);
        request.setToAccountId(toId);
        request.setAmount(new BigDecimal("100"));

        Account mockFromAccount = new Account();
        mockFromAccount.setId(fromId);

        when(accountRepository.findById(fromId)).thenReturn(Optional.of(mockFromAccount));
        when(accountRepository.findById(toId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.transfer(request);
        });
    }

    @Test
    void transfer_FromAccountIdResourceNotFound_shouldThrowResourceNotFoundException(){
        Long fromId = 1L;
        Long toId = 2L;

        TransferRequest request = new TransferRequest();
        request.setFromAccountId(fromId);
        request.setToAccountId(toId);
        request.setAmount(new BigDecimal("100"));

        when(accountRepository.findById(fromId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.transfer(request);
        });
    }
}