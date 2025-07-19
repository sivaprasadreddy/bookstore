package com.sivalabs.bookstore.admin.web;

import com.sivalabs.bookstore.catalog.CatalogAPI;
import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.catalog.core.models.UpdateBookCommand;
import com.sivalabs.bookstore.common.exceptions.ResourceNotFoundException;
import com.sivalabs.bookstore.common.model.PagedResult;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
class AdminBooksController {
    private final CatalogAPI catalogAPI;

    AdminBooksController(CatalogAPI catalogAPI) {
        this.catalogAPI = catalogAPI;
    }

    @GetMapping("/books")
    String showBooks(
            @RequestParam(name = "page", defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "10") int pageSize,
            Model model) {
        PagedResult<BookDto> pagedResult = catalogAPI.findBooks(pageNo, pageSize);
        model.addAttribute("pagedResult", pagedResult);
        return "admin/books";
    }

    @GetMapping("/books/{isbn}/edit")
    String showEditBookForm(@PathVariable String isbn, Model model) {
        BookDto book = catalogAPI
                .findBookByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for isbn: " + isbn));
        BookForm bookForm = BookForm.fromBookDto(book);
        model.addAttribute("bookForm", bookForm);
        return "admin/edit-book";
    }

    @PutMapping("/books/{isbn}")
    String updateBook(
            @PathVariable String isbn,
            @ModelAttribute("bookForm") @Valid BookForm bookForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/edit-book";
        }

        try {
            UpdateBookCommand cmd = bookForm.toUpdateBookCommand(isbn);
            catalogAPI.updateBook(cmd);
            redirectAttributes.addFlashAttribute("message", "Book updated successfully");
            return "redirect:/admin/books";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update book: " + e.getMessage());
            return "redirect:/admin/books";
        }
    }
}
