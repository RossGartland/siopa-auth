package com.siopa.siopa_auth.services;

import com.siopa.siopa_auth.models.ERole;
import com.siopa.siopa_auth.models.Role;
import com.siopa.siopa_auth.models.User;
import com.siopa.siopa_auth.payload.request.SignupRequest;
import com.siopa.siopa_auth.repositories.RoleRepository;
import com.siopa.siopa_auth.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setForename("John");
        signupRequest.setSurname("Doe");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setRole(Set.of("ROLE_USER"));

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));

        ResponseEntity<?> response = authService.registerUser(signupRequest);

        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameTaken() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("existingUser");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        ResponseEntity<?> response = authService.registerUser(signupRequest);

        assertEquals(400, response.getStatusCodeValue());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_RoleNotFound() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testuser");
        signupRequest.setRole(Set.of("ROLE_UNKNOWN"));

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(roleRepository.findByName(any(ERole.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(signupRequest);
        });

        assertTrue(exception.getMessage().contains("Role is not found"));
    }
}
