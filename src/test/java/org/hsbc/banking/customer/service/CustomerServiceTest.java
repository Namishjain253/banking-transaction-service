package org.hsbc.banking.customer.service;

import org.hsbc.banking.customer.dto.CustomerRequest;
import org.hsbc.banking.customer.dto.CustomerResponse;
import org.hsbc.banking.customer.entity.Customer;
import org.hsbc.banking.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @Test
    void getAllCustomer_shouldReturnAllCustomers(){
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        List<Customer> customers =  Arrays.asList(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customers);
        List<Customer> result = customerService.getAllCustomers();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(customers, result);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void createCustomerFindByEmail_shouldReturnIllegalArgumentException(){
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setEmail("abc@gmail.com");
        Customer customer = new Customer();
        when(customerRepository.findByEmail("abc@gmail.com")).thenReturn(Optional.of(customer));
        assertThrows(IllegalArgumentException.class, ()-> customerService.createCustomer(customerRequest));
    }

    @Test
    void createCustomerFindByMobileNumber_shouldReturnIllegalArgumentException(){
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setMobileNumber("1234567890");
        Customer customer = new Customer();
        when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(customer));
        assertThrows(IllegalArgumentException.class, ()-> customerService.createCustomer(customerRequest));
    }

    @Test
    void createCustomer_shouldReturnCustomer(){
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setMobileNumber("1234567890");
        customerRequest.setEmail("abc@gmail.com");
        customerRequest.setFirstName("ABC");
        customerRequest.setLastName("PQR");
        Customer customer = new Customer();
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerResponse result = customerService.createCustomer(customerRequest);
        assertNotNull(result);
        verify(customerRepository).findByEmail("abc@gmail.com");
        verify(customerRepository).findByMobileNumber("1234567890");
        verify(customerRepository).save(any(Customer.class));

    }
}
