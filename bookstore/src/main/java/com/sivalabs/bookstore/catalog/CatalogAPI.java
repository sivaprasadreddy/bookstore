package com.sivalabs.bookstore.catalog;

import com.sivalabs.bookstore.catalog.core.BookService;
import com.sivalabs.bookstore.catalog.core.models.BookDto;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CatalogAPI {

    private final BookService bookService;

    CatalogAPI(BookService bookService) {
        this.bookService = bookService;
    }

    public Optional<BookDto> findBookByIsbn(String isbn) {
        return bookService.findBookByIsbn(isbn);
    }
}
