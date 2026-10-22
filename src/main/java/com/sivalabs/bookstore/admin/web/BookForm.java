package com.sivalabs.bookstore.admin.web;

import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.catalog.core.models.UpdateBookCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record BookForm(
        Long id,
        @NotBlank(message = "ISBN is required") String isbn,
        @NotBlank(message = "Name is required") String name,
        String description,
        String imageUrl,

        @NotNull(message = "Price is required") @Positive(message = "Price must be greater than zero") BigDecimal price) {
    public static BookForm fromBookDto(BookDto bookDto) {
        return new BookForm(
                bookDto.getId(),
                bookDto.getIsbn(),
                bookDto.getName(),
                bookDto.getDescription(),
                bookDto.getImageUrl(),
                bookDto.getPrice());
    }

    public UpdateBookCommand toUpdateBookCommand(String isbn) {
        if (id == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        return new UpdateBookCommand(id, isbn, name, description, imageUrl, price);
    }
}
