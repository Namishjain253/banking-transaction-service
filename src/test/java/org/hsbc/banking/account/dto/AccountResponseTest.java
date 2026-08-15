package org.hsbc.banking.account.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountResponseTest {
    @Test
    void testDefaultConstructor(){
        AccountResponse response = new AccountResponse();
        assertNotNull(response);
    }
}
