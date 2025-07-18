package com.sivalabs.bookstore.catalog.web;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.catalog.core.BookService;
import com.sivalabs.bookstore.catalog.core.models.FindBooksQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final ApplicationProperties properties;

    BookController(BookService bookService, ApplicationProperties properties) {
        this.bookService = bookService;
        this.properties = properties;
    }

    @GetMapping("")
    String index() {
        return "redirect:/books";
    }

    @GetMapping("/books")
    String showBooks(@RequestParam(defaultValue = "1") int page, Model model) {
        log.info("Fetching books for page: {}", page);
        var query = new FindBooksQuery(page, properties.pageSize());
        var booksPage = bookService.findBooks(query);
        model.addAttribute("booksPage", booksPage);
        return "catalog/books";
    }
}
