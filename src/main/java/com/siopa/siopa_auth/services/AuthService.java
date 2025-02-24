package com.siopa.siopa_auth.services;

import com.siopa.siopa_auth.models.ERole;
import com.siopa.siopa_auth.models.Role;
import com.siopa.siopa_auth.models.User;
import com.siopa.siopa_auth.payload.request.LoginRequest;
import com.siopa.siopa_auth.payload.request.SignupRequest;
import com.siopa.siopa_auth.payload.response.JwtResponse;
import com.siopa.siopa_auth.payload.response.MessageResponse;
import com.siopa.siopa_auth.repositories.RoleRepository;
import com.siopa.siopa_auth.repositories.UserRepository;
import com.siopa.siopa_auth.security.jwt.JwtUtils;
import com.siopa.siopa_auth.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for auth requests.
 * Contains business logic.
 */
@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    /**
     * Gets user details corresponding to a given id.
     *
     * @param userID
     * @return
     */
    public Optional<User> getUserDetails(UUID userID) {return userRepository.findById(userID);}

    /**
     * Sign in user.
     * Authenticates username and password.
     * Sets security context with new authenticated user.
     * Creates the JWT.
     * @param loginRequest
     * @return User details and JWT.
     */
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));//Authenticates username and password.
        SecurityContextHolder.getContext().setAuthentication(authentication);//Sets security context with new authenticated user.
        String jwt = jwtUtils.generateJwtToken(authentication);//Creates the JWT.

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    /**
     * Signs a user up.
     * Verifies username and email don't already exist.
     * Creates a new user object and inserts it into the database.
     * @param signUpRequest
     * @return
     */
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }//Verifies username and email don't already exist.
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getForename(),
                signUpRequest.getSurname(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())); // Create new user's account
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole); //Sets role as user if no roles are specified.
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "owner":
                        Role modRole = roleRepository.findByName(ERole.ROLE_STORE_OWNER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                } //Sets role corresponding to the provided role.
            });
        }
        user.setRoles(roles);
        userRepository.save(user); //Save user to the database.
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
