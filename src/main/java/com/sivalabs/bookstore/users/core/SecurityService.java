package com.sivalabs.bookstore.users.core;

import java.util.Optional;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    Optional<Long> findLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return Optional.of(securityUser.getUserId());
        }
        return Optional.empty();
    }

    public Long getLoginUserId() {
        return findLoginUser().orElseThrow(() -> new InternalAuthenticationServiceException("Not logged in"));
    }
}
