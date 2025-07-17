package com.sivalabs.bookstore.users;

import com.sivalabs.bookstore.users.core.User;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    // private final UserAPI userAPI;

    public User loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return securityUser.getUser();
        }

        /*if (principal instanceof UserDetails userDetails) {
            return userAPI.findByEmail(userDetails.getUsername()).orElse(null);
        }*/
        return null;
    }

    public Optional<Long> getLoginUserId() {
        return Optional.ofNullable(loginUser()).map(User::getId);
    }
}
