package org.hsbc.banking.account.service;

import org.hsbc.banking.account.dto.AccountRequest;
import org.hsbc.banking.account.entity.Account;
import org.hsbc.banking.account.repository.AccountRepository;
import org.hsbc.banking.customer.entity.Customer;
import org.hsbc.banking.customer.repository.CustomerRepository;
import org.hsbc.banking.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository,
                          CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Account createAccount(AccountRequest request) {

        // 1. Find customer
        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Customer not found"));

        // 2. Check duplicate account number
        if (accountRepository
                .existsByAccountNumber(request.getAccountNumber())) {

            throw new IllegalArgumentException(
                    "Account number already exists");
        }

        Account account = new Account();

        account.setAccountNumber(request.getAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setCustomer(customer);

        return accountRepository.save(account);

    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found with id: " + id));
    }

    public Account updateAccount(Long id, Account account) {

        Account existingAccount = getAccountById(id);

        existingAccount.setAccountNumber(
                account.getAccountNumber());

        existingAccount.setAccountType(
                account.getAccountType());

        existingAccount.setBalance(
                account.getBalance());

        existingAccount.setCustomer(
                account.getCustomer());

        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long id) {

        Account account = getAccountById(id);

        accountRepository.delete(account);
    }
}