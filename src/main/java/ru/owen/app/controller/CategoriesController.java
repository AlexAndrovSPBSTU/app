package ru.owen.app.controller;

import org.springframework.web.bind.annotation.*;
import ru.owen.app.dto.OwenCategoryDTO;
import ru.owen.app.dto.ProductDTO;
import ru.owen.app.model.Mutual.CategoriesResponse;
import ru.owen.app.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public CategoriesResponse getCategories() {
        return categoryService.getMainCategories();
    }

    @GetMapping("/{categoryId}")
    public List<OwenCategoryDTO> getSubCategories(@PathVariable("categoryId") String categoryId) {
        return categoryService.getSubCategories(categoryId);
    }

    @GetMapping("/{categoryId}/products")
    public List<?> getProducts(@PathVariable("categoryId") String categoryId, @RequestParam(required = false) String query) {
        if (query != null) {
            return categoryService.getProductsByCategory(categoryId, query);
        }
        return categoryService.getProductsByCategory(categoryId);
    }

    @GetMapping("/{categoryId}/products/{productId}")
    public ProductDTO getProductByCategory(@PathVariable("categoryId") String categoryId,
                                           @PathVariable("productId") String productId) {
        return categoryService.getProductByCategory(productId, categoryId);
    }

}
