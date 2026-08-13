package org.hsbc.banking.transaction.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.hsbc.banking.transaction.dto.TransactionRequest;
import org.hsbc.banking.transaction.dto.TransferRequest;
import org.hsbc.banking.transaction.entity.Transaction;
import org.hsbc.banking.transaction.repository.TransactionRepository;
import org.hsbc.banking.transaction.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    public TransactionController(TransactionRepository transactionRepository,
                                 TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Transaction not found with id: " + id));

        transactionRepository.delete(transaction);

        return "Transaction deleted successfully";
    }

    @PostMapping("/deposit/{accountId}")
    public Transaction deposit(@PathVariable Long accountId,
                               @RequestBody TransactionRequest request) {

        return transactionService.deposit(accountId, request);
    }

    @PostMapping("/withdraw/{accountId}")
    public Transaction withdraw(@PathVariable Long accountId,
                                @Valid @RequestBody TransactionRequest request) {

        return transactionService.withdraw(accountId, request);
    }

    @PostMapping("/transfer")
    public String transfer(
            @Valid @RequestBody TransferRequest request) {

        transactionService.transfer(request);

        return "Transfer completed successfully";
    }
}