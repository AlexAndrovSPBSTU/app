package ru.owen.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.owen.app.constants.ProjectConstants;
import ru.owen.app.dto.OwenCategoryDTO;
import ru.owen.app.dto.ProductDTO;
import ru.owen.app.model.CategoriesResponse;
import ru.owen.app.model.CompositeIdClasses.OwenProductId;
import ru.owen.app.model.Owen.OwenCategory;
import ru.owen.app.model.SearchResponse;
import ru.owen.app.repository.*;

import java.util.List;

@Service
public class CategoryService {
    private final OwenCategoryRepository owenCategoryRepository;
    private final KippriborMeyrtecCategoryRepository kippriborMeyrtecCategoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final OwenProductRepository owenProductRepository;
    private final KippriborMeretecPriceRepository kippriborMeretecPriceRepository;

    public CategoryService(OwenCategoryRepository owenCategoryRepository,
                           KippriborMeyrtecCategoryRepository kippriborMeyrtecCategoryRepository, ProductRepository productRepository, ModelMapper modelMapper, OwenProductRepository owenProductRepository, KippriborMeretecPriceRepository kippriborMeretecPriceRepository) {
        this.owenCategoryRepository = owenCategoryRepository;
        this.kippriborMeyrtecCategoryRepository = kippriborMeyrtecCategoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.owenProductRepository = owenProductRepository;
        this.kippriborMeretecPriceRepository = kippriborMeretecPriceRepository;
    }

    public CategoriesResponse getMainCategories() {
        return CategoriesResponse.builder().owen(owenCategoryRepository.findALlByParentIsNull().stream().map(c -> modelMapper.map(c, OwenCategoryDTO.class)).toList())
                .meyrtec(kippriborMeyrtecCategoryRepository.findByIdStartingWith(ProjectConstants.MEYRTEC_PREFIX))
                .kippribor(kippriborMeyrtecCategoryRepository.findByIdStartingWith(ProjectConstants.KIPPRIBOR_PREFIX)).build();
    }

    public List<OwenCategoryDTO> getSubCategories(String categoryId) {
        return owenCategoryRepository.
                findAllByParentId(categoryId)
                .stream().map(c -> modelMapper.map(c, OwenCategoryDTO.class)).toList();
    }

    public List<?> getProductsByCategory(String categoryId) {
        if (owenCategoryRepository.findById(categoryId).isPresent()) {
            return owenCategoryRepository.findById(categoryId).get().getOwenProducts();
        }
        return kippriborMeyrtecCategoryRepository.findById(categoryId).get().getPrices();
    }

    public List<?> getProductsByCategory(String categoryId, String query) {
        if (owenCategoryRepository.findById(categoryId).isPresent()) {
            return owenCategoryRepository.findById(categoryId).get().getOwenProducts().stream().filter(product ->
                    product.getName().toLowerCase().contains(query.toLowerCase())).toList();
        }
        return kippriborMeyrtecCategoryRepository.findById(categoryId).get().getPrices().stream().filter(price ->
                price.getName().toLowerCase().contains(query.toLowerCase())).toList();
    }

    public ProductDTO getProductByCategory(String productId, String categoryId) {
        return modelMapper.map(productRepository.findById(OwenProductId.builder().id(productId)
                .owenCategory(OwenCategory.builder().id(categoryId).build())
                .build()).get(), ProductDTO.class);
    }


    public SearchResponse findProductsByQuery(String query) {
        return SearchResponse.builder()
                .owenProducts(owenProductRepository.findAllByNameContainingIgnoreCase(query))
                .kippriborPrices(kippriborMeretecPriceRepository.findAllKippriborProducts(query))
                .meyrtecPrices(kippriborMeretecPriceRepository.findAllMeyrtecProducts(query))
                .build();
    }
}
