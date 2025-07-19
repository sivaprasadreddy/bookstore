package com.sivalabs.bookstore.admin.web;

import com.sivalabs.bookstore.catalog.CatalogAPI;
import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.common.model.PagedResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
class AdminProductsController {
    private final CatalogAPI catalogAPI;

    AdminProductsController(CatalogAPI catalogAPI) {
        this.catalogAPI = catalogAPI;
    }

    @GetMapping("/products")
    String showProducts(
            @RequestParam(name = "page", defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "10") int pageSize,
            Model model) {
        PagedResult<BookDto> pagedResult = catalogAPI.findBooks(pageNo, pageSize);
        model.addAttribute("pagedResult", pagedResult);
        return "admin/products";
    }
}
