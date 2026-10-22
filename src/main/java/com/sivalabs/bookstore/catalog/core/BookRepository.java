package com.sivalabs.bookstore.catalog.core;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByIsbn(String isbn);
}
