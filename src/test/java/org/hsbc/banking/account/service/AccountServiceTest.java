package org.hsbc.banking.account.service;

import org.hsbc.banking.account.dto.AccountRequest;
import org.hsbc.banking.account.entity.Account;
import org.hsbc.banking.account.repository.AccountRepository;
import org.hsbc.banking.customer.entity.Customer;
import org.hsbc.banking.customer.repository.CustomerRepository;
import org.hsbc.banking.exception.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void updateAccount_shouldUpdateAndSaveAccount(){
        Long accountId = 1L;

        AccountRequest request =  new AccountRequest();
        request.setAccountNumber("ACC123");
        request.setAccountType("SAVING");
        request.setBalance(new BigDecimal("5000.0"));
        request.setCustomerId(10L);

        Account existingAccount =  new Account();
        existingAccount.setAccountNumber("OLD123");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));

        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);

        Account result = accountService.updateAccount(accountId, request);

        assertNotNull(result);

        assertEquals("ACC123",existingAccount.getAccountNumber());
        assertEquals("SAVING",existingAccount.getAccountType());
        assertEquals(new BigDecimal("5000.0"),existingAccount.getBalance());

        assertNotNull(existingAccount.getCustomer());
        assertEquals(10L, existingAccount.getCustomer().getId());

        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(existingAccount);
    }

    @Test
    void deleteAccount_shouldDeleteAccount(){
        Account account =  new Account();
        account.setId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        accountService.deleteAccount(1L);

        verify(accountRepository).delete(account);
    }

    @Test
    void getAllAccounts_shouldReturnAccounts(){
        List<Account> accounts =  List.of(new Account(), new Account());

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();

        assertEquals(2, result.size());

        verify(accountRepository).findAll();
    }

    @Test
    void getById_whenAccountNotFound_shouldThrowException(){
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, ()-> accountService.getAccountById(1L));
    }

    @Test
    void createAccount_success(){
        AccountRequest accountRequest =  new AccountRequest();
        accountRequest.setAccountNumber("12345");
        accountRequest.setCustomerId(1L);

        Customer customer =  new Customer();
        customer.setId(1L);

        Account account =  new Account();
        account.setAccountNumber("12345");
        account.setCustomer(customer);

        when(accountRepository.existsByAccountNumber("12345")).thenReturn(false);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result =  accountService.createAccount(accountRequest);

        assertNotNull(result);
        verify(customerRepository).findById(1L);
        verify(accountRepository).existsByAccountNumber("12345");
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_whenAccountNumberExist_shouldThrowExceprion(){
        AccountRequest accountRequest =  new AccountRequest();
        accountRequest.setAccountNumber("12345");
        accountRequest.setCustomerId(1L);

        Customer customer =  new Customer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        when(accountRepository.existsByAccountNumber("12345")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, ()-> accountService.createAccount(accountRequest));
    }

    @Test
    void createAccount_whenCustomerNotFound_shouldThrowIllegalArgumentException(){
        AccountRequest accountRequest =  new AccountRequest();
        accountRequest.setAccountNumber("12345");
        accountRequest.setCustomerId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()-> accountService.createAccount(accountRequest));
    }
}
