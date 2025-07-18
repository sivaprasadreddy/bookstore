package com.sivalabs.bookstore.catalog.core;

public class BooksImportException extends RuntimeException {
    public BooksImportException(String message) {
        super(message);
    }

    public BooksImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
