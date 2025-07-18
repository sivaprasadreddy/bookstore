package com.sivalabs.bookstore.users.core;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
