package com.sivalabs.bookstore.catalog.core;

import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.catalog.core.models.CreateBookCommand;
import com.sivalabs.bookstore.catalog.core.models.FindBooksQuery;
import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public void createBook(CreateBookCommand cmd) {
        BookDto book = new BookDto(null, cmd.isbn(), cmd.name(), cmd.description(), cmd.imageUrl(), cmd.price());
        BookEntity entity = bookMapper.toEntity(book);
        bookRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public PagedResult<BookDto> findBooks(FindBooksQuery query) {
        int pageNo = query.pageNo();
        int pageSize = query.pageSize();
        int page = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "name");
        Page<BookDto> booksPage = bookRepository.findAll(pageable).map(bookMapper::toDto);
        return new PagedResult<>(booksPage);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "#isbn", unless = "#result == null")
    public Optional<BookDto> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).map(bookMapper::toDto);
    }
}
