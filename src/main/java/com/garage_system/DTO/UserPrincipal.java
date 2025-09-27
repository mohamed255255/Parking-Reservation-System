package com.garage_system.DTO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.garage_system.Model.User;
public class UserPrincipal implements UserDetails {

    private final User user; // the User entity
    private List<GrantedAuthority> roles;

    public UserPrincipal(User user) {
        this.user = user;
        this.roles = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
   
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public int getId() {
        return user.getId();
    }
}
