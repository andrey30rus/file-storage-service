package com.storage.filestorageservice.auth.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.storage.filestorageservice.user.domain.User;
import lombok.Getter;

public class SecurityUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private final long id;

    private final String username;

    private final String passwordHash;

    public SecurityUser(long id, String username, String password) {
        this.id           = id;
        this.username     = username;
        this.passwordHash = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public SecurityUser(User user) {
        this.id           = user.getId();
        this.username     = user.getUsername();
        this.passwordHash = user.getPassword();
    }

}
