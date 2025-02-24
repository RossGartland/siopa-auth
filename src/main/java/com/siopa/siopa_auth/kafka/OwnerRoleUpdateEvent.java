package com.siopa.siopa_auth.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO class for updating the role of a user when they are added as an owner of a store.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerRoleUpdateEvent {
    private UUID userId;
    private String role;
}
