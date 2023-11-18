package ru.owen.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.owen.app.dto.OwenCategoryDTO;
import ru.owen.app.dto.ProductDTO;
import ru.owen.app.model.CategoriesResponse;
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
    public CategoriesResponse getMainCategories() {
        return categoryService.getMainCategories();
    }

    @GetMapping("/{categoryId}")
    public List<OwenCategoryDTO> getSubCategories(@PathVariable("categoryId") String categoryId) {
        return categoryService.getSubCategories(categoryId);
    }

    @GetMapping("/{categoryId}/products")
    public List<?> getProducts(@PathVariable("categoryId") String categoryId) {
        return categoryService.getProductsByCategory(categoryId);
    }

    @GetMapping("/{categoryId}/products/{productId}")
    public ProductDTO getProductByCategory(@PathVariable("categoryId") String categoryId,
                                           @PathVariable("productId") String productId) {
        return categoryService.getProductByCategory(productId, categoryId);
    }

}
