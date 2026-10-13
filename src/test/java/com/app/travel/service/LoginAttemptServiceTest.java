package com.app.travel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.app.travel.model.LoginAttempt;
import com.app.travel.repos.LoginAttemptRepository;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {

    @Mock
    private LoginAttemptRepository repository;

    private LoginAttemptService service;

    @BeforeEach
    void setUp() {
        service = new LoginAttemptService();
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    void firstFailedLoginCreatesAttemptRecord() {
        when(repository.findByUsername("traveler")).thenReturn(Optional.empty());

        service.loginFailed("traveler");

        ArgumentCaptor<LoginAttempt> savedAttempt = ArgumentCaptor.forClass(LoginAttempt.class);
        verify(repository).save(savedAttempt.capture());
        assertEquals("traveler", savedAttempt.getValue().getUsername());
        assertEquals(1, savedAttempt.getValue().getAttempts());
    }

    @Test
    void fifthFailedLoginSetsLockDuration() {
        LoginAttempt attempt = new LoginAttempt("traveler");
        attempt.setAttempts(4);
        when(repository.findByUsername("traveler")).thenReturn(Optional.of(attempt));
        LocalDateTime beforeFailure = LocalDateTime.now();

        service.loginFailed("traveler");

        assertEquals(5, attempt.getAttempts());
        assertTrue(attempt.getLockedUntil().isAfter(beforeFailure.plusMinutes(14)));
        assertTrue(attempt.getLockedUntil().isBefore(beforeFailure.plusMinutes(16)));
    }

    @Test
    void activeLockBlocksLogin() {
        LoginAttempt attempt = new LoginAttempt("traveler");
        attempt.setAttempts(5);
        attempt.setLockedUntil(LocalDateTime.now().plusMinutes(1));
        when(repository.findByUsername("traveler")).thenReturn(Optional.of(attempt));

        assertTrue(service.isBlocked("traveler"));
        verify(repository, never()).deleteByUsername("traveler");
    }

    @Test
    void expiredLockIsDeletedAndDoesNotBlockLogin() {
        LoginAttempt attempt = new LoginAttempt("traveler");
        attempt.setAttempts(5);
        attempt.setLockedUntil(LocalDateTime.now().minusMinutes(1));
        when(repository.findByUsername("traveler")).thenReturn(Optional.of(attempt));

        assertFalse(service.isBlocked("traveler"));
        verify(repository).deleteByUsername("traveler");
    }

    @Test
    void remainingAttemptsAndSuccessfulLoginAreHandled() {
        LoginAttempt attempt = new LoginAttempt("traveler");
        attempt.setAttempts(3);
        when(repository.findByUsername("traveler")).thenReturn(Optional.of(attempt));
        when(repository.findByUsername("new-traveler")).thenReturn(Optional.empty());

        assertEquals(2, service.getRemainingAttempts("traveler"));
        assertEquals(5, service.getRemainingAttempts("new-traveler"));

        service.loginSucceeded("traveler");

        verify(repository).deleteByUsername("traveler");
    }
}