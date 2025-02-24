package com.siopa.siopa_auth.security.services;

import com.siopa.siopa_auth.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contains modelling attributes user details entity.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private String username;
    private String forename;
    private String surname;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities; //Stores the users permissions.

    /**
     * Constructor for user details implementation.
     * @param userId
     * @param username
     * @param forename
     * @param surname
     * @param email
     * @param password
     * @param authorities
     */
    public UserDetailsImpl(UUID userId, String username, String forename, String surname, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Builds user details object.
     * @param user
     * @return
     */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList()); //Creates role list for user.
        return new UserDetailsImpl(
                user.getUserId(),
                user.getUsername(),
                user.getForename(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public UUID getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }
    public String getForename() {
        return forename;
    }
    public String getSurname() {
        return surname;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(userId, userId);
    }
}