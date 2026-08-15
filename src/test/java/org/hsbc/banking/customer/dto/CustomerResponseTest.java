package org.hsbc.banking.customer.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerResponseTest {
    @Test
    void testDefaultConstructor(){
        CustomerResponse response = new CustomerResponse();
        assertNotNull(response);
    }
}
