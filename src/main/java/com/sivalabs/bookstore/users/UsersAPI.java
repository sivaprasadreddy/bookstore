package com.sivalabs.bookstore.users;

import com.sivalabs.bookstore.users.core.SecurityService;
import org.springframework.stereotype.Service;

@Service
public class UsersAPI {
    private final SecurityService securityService;

    UsersAPI(SecurityService securityService) {
        this.securityService = securityService;
    }

    public Long getLoginUserId() {
        return securityService.getLoginUserId();
    }
}
