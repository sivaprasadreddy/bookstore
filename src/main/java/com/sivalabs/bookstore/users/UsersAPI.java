package com.sivalabs.bookstore.users;

import com.sivalabs.bookstore.users.core.User;
import com.sivalabs.bookstore.users.core.UserService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsersAPI {
    private final UserService userService;

    public UsersAPI(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> findByEmail(String email) {
        return userService.findByEmail(email);
    }
}
