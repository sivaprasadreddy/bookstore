package com.sivalabs.bookstore.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class SecurityUserDetailsService implements UserDetailsService {
    private final UsersAPI usersAPI;

    public SecurityUserDetailsService(UsersAPI usersAPI) {
        this.usersAPI = usersAPI;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return usersAPI.findByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
    }
}
