package com.siopa.siopa_auth.models;

import jakarta.persistence.*;


/**
 * Represents the role entity.
 * This entity stores user roles such as ROLE_USER, ROLE_ADMIN, and ROLE_STORE_OWNER.
 * Roles are used for managing authentication and authorization in the system.
 */
@Entity
@Table(name = "roles")
public class Role {

    /**
     * Primary key for the role entity.
     * Auto-generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Stores the role name as an enumerated type.
     * Uses EnumType.STRING to store the name as a string in the database.
     * Example values: "ROLE_USER", "ROLE_ADMIN", "ROLE_STORE_OWNER".
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name; // Use roles for the role enumerator.

    /**
     * Default constructor required by JPA.
     */
    public Role() {
    }

    /**
     * Parameterized constructor to create a role with a specific name.
     * @param name The role name (e.g., ROLE_USER, ROLE_ADMIN).
     */
    public Role(ERole name) {
        this.name = name;
    }

    /**
     * Retrieves the role ID.
     * @return The ID of the role.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the role ID.
     * @param id The new ID for the role.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retrieves the role name.
     * @return The role name as an ERole enum.
     */
    public ERole getName() {
        return name;
    }

    /**
     * Sets the role name.
     * @param name The new role name (must be an ERole value).
     */
    public void setName(ERole name) {
        this.name = name;
    }
}

