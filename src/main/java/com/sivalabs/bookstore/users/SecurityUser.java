package com.sivalabs.bookstore.users;

import com.sivalabs.bookstore.users.core.User;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public SecurityUser(User user) {
        super(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name())));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
