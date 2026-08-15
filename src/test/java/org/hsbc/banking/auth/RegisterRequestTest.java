package org.hsbc.banking.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegisterRequestTest {
    @Test
    void testDefaultConstructor(){
        RegisterRequest response = new RegisterRequest();
        assertNotNull(response);
    }
}
