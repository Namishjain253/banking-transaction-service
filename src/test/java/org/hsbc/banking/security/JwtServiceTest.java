package org.hsbc.banking.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;

    private final String secret = "my-super-secret-key-for-jwt-testing-12345678901234567890";

    @BeforeEach
    void setUp() throws Exception{
        jwtService = new JwtService();

        Field secretField = JwtService.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtService,secret);
    }

    @Test
    void testGenerateToken(){
        String token = jwtService.generateToken("testuser");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void testExtractUsername(){
        String token = jwtService.generateToken("testuser");

        String username = jwtService.extractUsername(token);
        assertEquals("testuser",username);
    }

    @Test
    void testTokenValid(){
        String token = jwtService.generateToken("testuser");
        boolean result = jwtService.isTokenValid(token);

        assertTrue(result);
    }

    @Test
    void testTokenInvalid(){
        boolean result = jwtService.isTokenValid("invalid-token");
        assertFalse(result);
    }
}
