package com.siopa.siopa_auth.security.jwt;

import com.siopa.siopa_auth.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import java.util.Date;

/**
 * Utility class for decoding JWT, getting attribute from JWT and validating JWT.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${siopa-auth-service.app.jwtSecret}")
    private String jwtSecret;
    @Value("${siopa-auth-service.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generates JWT by providing username, expiration time, issued date and hashing secret.
     * @param authentication
     * @return
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date()) //Set issue data.
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) //Set expiry time.
                .signWith(SignatureAlgorithm.HS512, jwtSecret) //Hash JWT with secret.
                .compact();
    }

    /**
     * Gets the username from the JWT.
     * @param token
     * @return
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validates the JWT and catches errors that may occur/
     * @param authToken
     * @return
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}