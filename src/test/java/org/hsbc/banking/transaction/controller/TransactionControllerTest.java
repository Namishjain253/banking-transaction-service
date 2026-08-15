package org.hsbc.banking.transaction.controller;

import org.hsbc.banking.transaction.dto.TransactionRequest;
import org.hsbc.banking.transaction.dto.TransferRequest;
import org.hsbc.banking.transaction.entity.Transaction;
import org.hsbc.banking.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    @InjectMocks
    private TransactionController transactionController;

    @Mock
    TransactionService transactionService ;

    @Test
    void testTransactionControllerConstructor(){

        TransactionController controller = new TransactionController(transactionService);

        assertNotNull(controller);
    }

    @Test
    void testCreateTransaction(){
        TransactionRequest request = new TransactionRequest();
        Transaction transaction = new Transaction();

        when(transactionService.createTransaction(request)).thenReturn(transaction);

        Transaction result = transactionController.createTransaction(request);

        assertNotNull(result);
        verify(transactionService).createTransaction(request);
    }

    @Test
    void testGetAllTransaction(){
        List<Transaction> transactions = List.of(new Transaction());

        when(transactionService.getAllTransactions()).thenReturn(transactions);

        List<Transaction> result = transactionController.getAllTransactions();

        assertNotNull(result);
        assertEquals(1,result.size());
        verify(transactionService).getAllTransactions();
    }

    @Test
    void testDeleteTransaction(){
        Long transactionId = 1L;

        doNothing().when(transactionService).deleteTransaction(transactionId);

        String result = transactionController.deleteTransaction(transactionId);

        assertEquals("Transaction deleted successfully", result);
        verify(transactionService).deleteTransaction(transactionId);
    }

    @Test
    void testDeposit(){
        Long accountId = 1L;
        TransactionRequest request = new TransactionRequest();
        Transaction transaction =  new Transaction();

        when(transactionService.deposit(accountId,request)).thenReturn(transaction);

        Transaction result = transactionController.deposit(accountId,request);

        assertNotNull(result);
        verify(transactionService).deposit(accountId,request);
    }

    @Test
    void testWithdraw(){
        Long accountId = 1L;
        TransactionRequest request = new TransactionRequest();
        Transaction transaction =  new Transaction();

        when(transactionService.withdraw(accountId, request)).thenReturn(transaction);

        Transaction result = transactionController.withdraw(accountId, request);

        assertNotNull(result);
        verify(transactionService).withdraw(accountId,request);
    }

    @Test
    void testTransfer(){
        TransferRequest request = new TransferRequest();
        doNothing().when(transactionService).transfer(request);

        String result = transactionController.transfer(request);

        assertEquals("Transfer completed successfully", result);
        verify(transactionService).transfer(request);
    }
}
