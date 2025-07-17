package com.sivalabs.bookstore.catalog.core;

import com.sivalabs.bookstore.catalog.core.models.BookDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookDto> findByIsbn(String isbn);

    Page<BookDto> findAllBy(Pageable pageable);
}
