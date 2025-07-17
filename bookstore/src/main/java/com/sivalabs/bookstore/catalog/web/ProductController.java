package com.sivalabs.bookstore.catalog.web;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.catalog.core.CatalogService;
import com.sivalabs.bookstore.catalog.core.models.FindProductsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final CatalogService productService;
    private final ApplicationProperties properties;

    ProductController(CatalogService productService, ApplicationProperties properties) {
        this.productService = productService;
        this.properties = properties;
    }

    @GetMapping("")
    String index() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    String showProducts(@RequestParam(defaultValue = "1") int page, Model model) {
        log.info("Fetching products for page: {}", page);
        var query = new FindProductsQuery(page, properties.pageSize());
        var productsPage = productService.findProducts(query);
        model.addAttribute("productsPage", productsPage);
        return "products";
    }
}
