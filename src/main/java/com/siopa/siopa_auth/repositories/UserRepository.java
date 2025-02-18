package com.siopa.siopa_auth.repositories;

import com.siopa.siopa_auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Data access interface for user entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds user by a given username.
     * @param username
     * @return
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds user by a given id.
     * @param id
     * @return
     */
    List<User> findById(int id);

    /**
     * Checks if username exists.
     * @param username
     * @return true if username exists.
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if email exists.
     * @param email
     * @return true if email exists.
     */
    Boolean existsByEmail(String email);

    /**
     * Finds an account via an email address.
     * @param email
     * @return
     */
    User findByEmail(String email);
}