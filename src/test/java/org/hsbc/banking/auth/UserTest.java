package org.hsbc.banking.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {
    @Test
    void testDefaultConstructor(){
        User response = new User();
        assertNotNull(response);
    }
}
