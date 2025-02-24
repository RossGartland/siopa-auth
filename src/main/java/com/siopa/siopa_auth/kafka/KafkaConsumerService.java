package com.siopa.siopa_auth.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siopa.siopa_auth.models.ERole;
import com.siopa.siopa_auth.models.Role;
import com.siopa.siopa_auth.models.User;
import com.siopa.siopa_auth.repositories.RoleRepository;
import com.siopa.siopa_auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;


/**
 * Service for consuming Kafka messages related to user role updates.
 * Listens to the "user-role-updates" topic and updates user roles accordingly.
 */
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Kafka listener that processes user role update events.
     * Deserializes the incoming message, fetches the user, and assigns the store owner role if needed.
     *
     * @param message The incoming Kafka message in JSON format
     */
    @KafkaListener(topics = "user-role-updates", groupId = "auth-service-group")
    @Transactional
    public void consumeRoleUpdate(String message) {
        try {
            // Convert the Kafka message into an OwnerRoleUpdateEvent object
            OwnerRoleUpdateEvent event = objectMapper.readValue(message, OwnerRoleUpdateEvent.class);

            logger.info("Received Kafka message for user ID: {}", event.getUserId());

            // Retrieve the user from the database by UUID
            Optional<User> optionalUser = userRepository.findById(event.getUserId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Fetch the store owner role from the database
                Role ownerRole = roleRepository.findByName(ERole.ROLE_STORE_OWNER)
                        .orElseThrow(() -> new RuntimeException("Error: ROLE_STORE_OWNER not found."));

                // Check if the user already has the store owner role
                Set<Role> roles = user.getRoles();
                if (!roles.contains(ownerRole)) {
                    roles.add(ownerRole);
                    user.setRoles(roles);
                    userRepository.save(user);
                    logger.info("User {} has been assigned the ROLE_STORE_OWNER role", user.getUsername());
                } else {
                    logger.info("User {} already has the ROLE_STORE_OWNER role", user.getUsername());
                }
            } else {
                logger.warn("User with ID {} not found", event.getUserId());
            }
        } catch (Exception e) {
            logger.error("Error processing Kafka message: {}", e.getMessage(), e);
        }
    }
}