package com.sivalabs.bookstore.catalog.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sivalabs.bookstore.common.TestcontainersConfiguration;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAllInBatch();

        bookRepository.save(new BookEntity(null, "P100", "Book 1", "Book 1 desc", null, BigDecimal.TEN));
        bookRepository.save(new BookEntity(null, "P101", "Book 2", "Book 2 desc", null, BigDecimal.valueOf(24)));
    }

    @Test
    void shouldFailToSaveBookWithDuplicateIsbn() {
        var product = new BookEntity(null, "P100", "Book name", "Book desc", null, BigDecimal.TEN);
        assertThrows(DataIntegrityViolationException.class, () -> bookRepository.saveAndFlush(product));
    }

    @Test
    void shouldGetBookByIsbn() {
        Optional<BookEntity> optionalBook = bookRepository.findByIsbn("P100");
        assertThat(optionalBook).isNotEmpty();
        assertThat(optionalBook.get().getIsbn()).isEqualTo("P100");
        assertThat(optionalBook.get().getName()).isEqualTo("Book 1");
        assertThat(optionalBook.get().getDescription()).isEqualTo("Book 1 desc");
        assertThat(optionalBook.get().getPrice()).isEqualTo(BigDecimal.TEN);
    }
}
