package org.hsbc.banking.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp(){
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_whenAuthorizationHeaderIsNull_shouldContinueFilterChain() throws Exception{
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);

        verify(filterChain).doFilter(request,response);
        verifyNoInteractions(jwtService);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_whenAuthorizationHeaderDoesNotStartWithBearer_shouldContinueFilterChain() throws Exception{
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);

        verify(filterChain).doFilter(request,response);
        verifyNoInteractions(jwtService);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_whenTokenIsValid_shouldSetAuthentication() throws Exception{
        String token = "valid-token";
        String username = "testuser";

        when(request.getHeader("Authorization")).thenReturn("Bearer "+token);
        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.extractUsername(token)).thenReturn(username);

        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(username,authentication.getName());
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);

        verify(jwtService).isTokenValid(token);
        verify(jwtService).extractUsername(token);
        verify(filterChain).doFilter(request,response);
    }

    @Test
    void doFilterInternal_whenTokenIsInvalid_shouldNotSetAuthentication() throws Exception{
        String token = "invalid-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer "+ token);
        when(jwtService.isTokenValid(token)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(jwtService).isTokenValid(token);
        verify(jwtService,never()).extractUsername(token);
        verify(filterChain).doFilter(request,response);
    }

    @Test
    void doFilterInternal_whenValidToken_shouldUserCorrectTokenWithoutBearerPrefix() throws Exception{
        String token = "abc.def.xyz";
        String username = "abcdefg";

        when(request.getHeader("Authorization")).thenReturn("Bearer "+token);
        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.extractUsername(token)).thenReturn(username);

        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);

        verify(jwtService).isTokenValid(token);
        verify(jwtService).extractUsername(token);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(username,authentication.getName());
    }
}
