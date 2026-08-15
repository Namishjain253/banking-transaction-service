package org.hsbc.banking.customer.controller;

import org.hsbc.banking.customer.dto.CustomerRequest;
import org.hsbc.banking.customer.dto.CustomerResponse;
import org.hsbc.banking.customer.entity.Customer;
import org.hsbc.banking.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void testCreateCustomer(){
        CustomerRequest request = new CustomerRequest();
        CustomerResponse response = new CustomerResponse();

        when(customerService.createCustomer(request)).thenReturn(response);

        ResponseEntity<CustomerResponse> result = customerController.createCustomer(request);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED,result.getStatusCode());
        assertEquals(response,result.getBody());

        verify(customerService).createCustomer(request);
    }

    @Test
    void testGetAllCustomer(){
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());

        when(customerService.getAllCustomers()).thenReturn(customers);

        List<Customer> result = customerController.getAllCustomers();

        assertNotNull(result);
        assertEquals(1,result.size());
    }
}
