package com.sivalabs.bookstore.users.core;

import java.util.List;
import java.util.Objects;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class SecurityUser extends org.springframework.security.core.userdetails.User {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SecurityUser that = (SecurityUser) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(user);
        return result;
    }
}
