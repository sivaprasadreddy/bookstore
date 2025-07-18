package com.sivalabs.bookstore.catalog.core;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String msg) {
        super(msg);
    }

    public static BookNotFoundException withIsbn(String isbn) {
        return new BookNotFoundException("Book with ISBN: " + isbn + " not found");
    }
}
