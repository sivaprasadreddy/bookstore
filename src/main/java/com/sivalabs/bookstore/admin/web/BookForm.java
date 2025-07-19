package com.sivalabs.bookstore.admin.web;

import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.catalog.core.models.UpdateBookCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record BookForm(
        Long id,
        @NotBlank(message = "ISBN is required") @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters") String isbn,
        @NotBlank(message = "Name is required") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,
        String description,
        String imageUrl,
        @NotNull(message = "Price is required") @Positive(message = "Price must be greater than zero") BigDecimal price) {
    public static BookForm fromBookDto(BookDto bookDto) {
        return new BookForm(
                bookDto.id(),
                bookDto.isbn(),
                bookDto.name(),
                bookDto.description(),
                bookDto.imageUrl(),
                bookDto.price());
    }

    public UpdateBookCommand toUpdateBookCommand(String isbn) {
        return new UpdateBookCommand(id, isbn, name, description, imageUrl, price);
    }
}
