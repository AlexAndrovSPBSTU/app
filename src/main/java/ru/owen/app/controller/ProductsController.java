package ru.owen.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.owen.app.model.Mutual.SearchResponse;
import ru.owen.app.service.CategoryService;

@RestController
@RequestMapping("products")
public class ProductsController {
    private final CategoryService categoryService;

    public ProductsController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public SearchResponse getProducts(@RequestParam String query) {
        return categoryService.findProductsByQuery(query);
    }
}
