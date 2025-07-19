package com.sivalabs.bookstore.catalog;

import com.sivalabs.bookstore.catalog.core.BookService;
import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.catalog.core.models.FindBooksQuery;
import com.sivalabs.bookstore.common.model.PagedResult;
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
    
    public PagedResult<BookDto> findBooks(int pageNo, int pageSize) {
        FindBooksQuery query = new FindBooksQuery(pageNo, pageSize);
        return bookService.findBooks(query);
    }
}
