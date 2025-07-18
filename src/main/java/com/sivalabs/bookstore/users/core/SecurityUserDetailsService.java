package com.sivalabs.bookstore.users.core;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class SecurityUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public SecurityUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userService
                .findByEmail(username)
                .map(u -> new SecurityUser(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.getRole()))
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
    }
}
