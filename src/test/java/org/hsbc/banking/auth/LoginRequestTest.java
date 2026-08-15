package org.hsbc.banking.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoginRequestTest {
    @Test
    void testDefaultConstructor(){
        LoginRequest response = new LoginRequest();
        assertNotNull(response);
    }
}
