package com.sivalabs.bookstore.catalog.core;

import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.catalog.core.models.CreateBookCommand;
import com.sivalabs.bookstore.catalog.core.models.FindBooksQuery;
import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void createBook(CreateBookCommand cmd) {
        BookDto book = new BookDto(null, cmd.isbn(), cmd.name(), cmd.description(), cmd.imageUrl(), cmd.price());
        bookRepository.save(toEntity(book));
    }

    @Transactional(readOnly = true)
    public PagedResult<BookDto> findBooks(FindBooksQuery query) {
        int pageNo = query.pageNo();
        int pageSize = query.pageSize();
        int page = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "name");
        Page<BookDto> booksPage = bookRepository.findAllBy(pageable);
        return new PagedResult<>(booksPage);
    }

    @Transactional(readOnly = true)
    public Optional<BookDto> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    private BookEntity toEntity(BookDto book) {
        return new BookEntity(book.id(), book.isbn(), book.name(), book.description(), book.imageUrl(), book.price());
    }
}
