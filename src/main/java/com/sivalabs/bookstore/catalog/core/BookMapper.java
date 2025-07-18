package com.sivalabs.bookstore.catalog.core;

import com.sivalabs.bookstore.catalog.core.models.BookDto;
import org.springframework.stereotype.Component;

@Component
class BookMapper {

    public BookDto toDto(BookEntity book) {
        return new BookDto(
                book.getId(),
                book.getIsbn(),
                book.getName(),
                book.getDescription(),
                book.getImageUrl(),
                book.getPrice());
    }

    public BookEntity toEntity(BookDto book) {
        return new BookEntity(book.id(), book.isbn(), book.name(), book.description(), book.imageUrl(), book.price());
    }
}
