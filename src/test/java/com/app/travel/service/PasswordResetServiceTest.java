package com.app.travel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.app.travel.model.ResetToken;
import com.app.travel.model.Users;
import com.app.travel.repos.ResetTokenRepository;
import com.app.travel.repos.UserRepository;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    private static final String EMAIL = "traveler@example.com";

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResetTokenRepository resetTokenRepository;

    private PasswordResetService service;

    @BeforeEach
    void setUp() {
        service = new PasswordResetService();
        ReflectionTestUtils.setField(service, "userRepos", userRepository);
        ReflectionTestUtils.setField(service, "resetTokenRepos", resetTokenRepository);
    }

    @Test
    void forgotPasswordRejectsNullAndEmptyEmail() {
        assertThrows(IllegalArgumentException.class, () -> service.forgotPassword(null));
        assertThrows(IllegalArgumentException.class, () -> service.forgotPassword(""));

        verifyNoInteractions(userRepository, resetTokenRepository);
    }

    @Test
    void forgotPasswordInvalidatesOldTokenAndSavesNewToken() {
        ResetToken previousToken = new ResetToken();
        when(resetTokenRepository.findByEmail(EMAIL)).thenReturn(Optional.of(previousToken));

        service.forgotPassword(EMAIL);

        ArgumentCaptor<ResetToken> savedTokens = ArgumentCaptor.forClass(ResetToken.class);
        verify(resetTokenRepository, times(2)).save(savedTokens.capture());
        List<ResetToken> tokens = savedTokens.getAllValues();
        ResetToken createdToken = tokens.get(1);

        assertTrue(previousToken.isTokenUsed());
        assertSame(previousToken, tokens.get(0));
        assertEquals(EMAIL, createdToken.getEmail());
        assertNotNull(createdToken.getToken());
        assertFalse(createdToken.isTokenUsed());
        assertTrue(createdToken.getExpiration().after(new Date()));
    }

    @Test
    void validateTokenReturnsMatchingUnusedToken() {
        ResetToken resetToken = new ResetToken();
        when(resetTokenRepository.findByEmailAndTokenAndTokenUsed(EMAIL, "reset-token", false))
                .thenReturn(Optional.of(resetToken));

        assertSame(resetToken, service.validateToken(EMAIL, "reset-token").orElseThrow());
    }

    @Test
    void invalidResetTokenCannotChangePassword() {
        when(resetTokenRepository.findByEmailAndTokenAndTokenUsed(EMAIL, "invalid", false))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.changePassword(EMAIL, "invalid", "new-password"));

        verifyNoInteractions(userRepository);
    }

    @Test
    void validResetTokenIsConsumedAndPasswordIsEncoded() {
        ResetToken resetToken = new ResetToken();
        Users user = new Users();
        when(resetTokenRepository.findByEmailAndTokenAndTokenUsed(EMAIL, "reset-token", false))
                .thenReturn(Optional.of(resetToken));
        when(userRepository.findByEmail(EMAIL)).thenReturn(user);

        service.changePassword(EMAIL, "reset-token", "new-password");

        assertTrue(resetToken.isTokenUsed());
        assertNotNull(user.getPassword());
        assertTrue(new BCryptPasswordEncoder().matches("new-password", user.getPassword()));
        verify(resetTokenRepository).save(resetToken);
        verify(userRepository).save(user);
    }
}