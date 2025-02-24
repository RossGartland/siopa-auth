package com.siopa.siopa_auth.controllers;

import com.siopa.siopa_auth.models.User;
import com.siopa.siopa_auth.payload.request.LoginRequest;
import com.siopa.siopa_auth.payload.request.SignupRequest;
import com.siopa.siopa_auth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;


/**
 * Auth controller class.
 * Contains auth routes.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    /**
     * Route for logging in.
     * @param loginRequest
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    /**
     * Route for signing up.
     * @param signUpRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    /**
     * Route for getting details about a particular user.
     *
     * @param userID
     * @return
     */
    @GetMapping("/users/{userID}")
    public Optional<User> getUserDetails(@PathVariable UUID userID) {
        return authService.getUserDetails(userID);
    }
}