package com.siopa.siopa_auth.security.jwt;

import com.siopa.siopa_auth.models.User;
import com.siopa.siopa_auth.security.services.UserDetailsImpl;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JwtUtils();

        // Generate a secure 512-bit key
        Key secureKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String encodedKey = Base64.getEncoder().encodeToString(secureKey.getEncoded());

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", encodedKey);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000);
    }

    /**
     * Tests token validation.
     */
    @Test
    void testValidateToken() {
        Authentication authentication = createMockAuthentication("testuser");
        String token = jwtUtils.generateJwtToken(authentication);

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid, "Token should be valid");
    }

    /**
     * Tests extracting username from JWT token.
     */
    @Test
    void testExtractUsername() {
        Authentication authentication = createMockAuthentication("testuser");
        String token = jwtUtils.generateJwtToken(authentication);

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("testuser", extractedUsername, "Extracted username should match");
    }

    /**
     * Helper method to create a mock authentication object with a given username.
     */
    private Authentication createMockAuthentication(String username) {
        UserDetails userDetails = new UserDetailsImpl(1, username, "forename", "surname", "email",
                "password", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
