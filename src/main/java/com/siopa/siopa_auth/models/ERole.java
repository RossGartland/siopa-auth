package com.siopa.siopa_auth.models;

/**
 * Enum values for users role.
 */
public enum ERole {
    ROLE_USER, //Basic authenticated user.
    ROLE_STORE_OWNER, //User who owns at least 1 store.
    ROLE_ADMIN //Admin of the system.
}
