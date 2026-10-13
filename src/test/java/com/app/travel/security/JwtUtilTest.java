package com.app.travel.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.app.travel.service.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "test-secret-that-is-at-least-thirty-two-bytes-long");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 60_000);
        ReflectionTestUtils.setField(jwtUtil, "tokenBlacklistService", tokenBlacklistService);
        jwtUtil.init();
    }

    @Test
    void generatedTokenContainsUsernameAndIsValid() {
        String token = jwtUtil.generateToken("traveler");
        when(tokenBlacklistService.isBlacklisted(token)).thenReturn(false);

        assertEquals("traveler", jwtUtil.getUsernameFromToken(token));
        assertTrue(jwtUtil.validateJwtToken(token));
    }

    @Test
    void blacklistedTokenIsRejected() {
        String token = jwtUtil.generateToken("traveler");
        when(tokenBlacklistService.isBlacklisted(token)).thenReturn(true);

        assertFalse(jwtUtil.validateJwtToken(token));
    }

    @Test
    void malformedTokenIsRejected() {
        when(tokenBlacklistService.isBlacklisted("not-a-jwt")).thenReturn(false);

        assertFalse(jwtUtil.validateJwtToken("not-a-jwt"));
    }

    @Test
    void expiredTokenIsRejected() {
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", -60_000);
        String token = jwtUtil.generateToken("traveler");
        when(tokenBlacklistService.isBlacklisted(token)).thenReturn(false);

        assertFalse(jwtUtil.validateJwtToken(token));
    }

    @Test
    void extractsOnlyBearerAuthorizationHeaders() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer signed-token");

        assertEquals("signed-token", jwtUtil.extractTokenFromRequest(request));

        when(request.getHeader("Authorization")).thenReturn("Basic credentials");
        assertNull(jwtUtil.extractTokenFromRequest(request));
    }
}