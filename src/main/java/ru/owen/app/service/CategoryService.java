package ru.owen.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.owen.app.constants.ProjectConstants;
import ru.owen.app.dto.OwenCategoryDTO;
import ru.owen.app.dto.ProductDTO;
import ru.owen.app.model.CategoriesResponse;
import ru.owen.app.model.CompositeIdClasses.ProductId;
import ru.owen.app.model.Owen.OwenCategory;
import ru.owen.app.repository.KippriborMeyrtecCategoryRepository;
import ru.owen.app.repository.OwenCategoryRepository;
import ru.owen.app.repository.ProductRepository;

import java.util.List;

@Service
public class CategoryService {
    private final OwenCategoryRepository owenCategoryRepository;
    private final KippriborMeyrtecCategoryRepository kippriborMeyrtecCategoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CategoryService(OwenCategoryRepository owenCategoryRepository,
                           KippriborMeyrtecCategoryRepository kippriborMeyrtecCategoryRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.owenCategoryRepository = owenCategoryRepository;
        this.kippriborMeyrtecCategoryRepository = kippriborMeyrtecCategoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public CategoriesResponse getMainCategories() {
        return CategoriesResponse.builder().owen(owenCategoryRepository.findALlByParentIsNull().stream().map(c -> modelMapper.map(c, OwenCategoryDTO.class)).toList())
                .meyrtec(kippriborMeyrtecCategoryRepository.findByIdStartingWith(ProjectConstants.MEYRTEC_PREFIX))
                .kippribor(kippriborMeyrtecCategoryRepository.findByIdStartingWith(ProjectConstants.KIPPRIBOR_PREFIX)).build();
    }

    public List<OwenCategoryDTO> getSubCategories(String categoryId) {
        return owenCategoryRepository.findAllByParentId(categoryId).stream().map(c -> modelMapper.map(c, OwenCategoryDTO.class)).toList();
    }

    public List<?> getProductsByCategory(String categoryId) {
        if (owenCategoryRepository.findById(categoryId).isPresent()) {
            return owenCategoryRepository.findById(categoryId).get().getProducts();
        }
        return kippriborMeyrtecCategoryRepository.findById(categoryId).get().getPrices();
    }

    public ProductDTO getProductByCategory(String productId, String categoryId) {
        return modelMapper.map(productRepository.findById(ProductId.builder().id(productId)
                .owenCategory(OwenCategory.builder().id(categoryId).build()).build()).get(), ProductDTO.class);
    }

}
