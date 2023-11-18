package ru.owen.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.owen.app.constants.ProjectConstants;
import ru.owen.app.model.*;
import ru.owen.app.model.KippriborMeyrtec.*;
import ru.owen.app.model.Owen.*;
import ru.owen.app.repository.*;
import ru.owen.app.util.CsvToProductListParser;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

@Service
public class DbManagementService {
    private final CsvToProductListParser csvToProductListParser;
    private final ObjectMapper objectMapper;
    private final OwenCategoryRepository owenCategoryRepository;
    private final ProductRepository productRepository;
    private final DocRepository docRepository;
    private final DocItemRepository docItemRepository;
    private final ImageRepository imageRepository;
    private final ModificationRepository modificationRepository;
    private final OwenPriceRepository owenPriceRepository;
    private final KippriborMeretecPriceRepository kippriborMeyertecPriceRepository;
    private final ArrivalRepository arrivalRepository;
    private final KippriborMeyrtecCategoryRepository kippriborMeyrtecCategoryRepository;
    private final Logger logger = Logger.getLogger(DbManagementService.class.getName());

    @Autowired
    public DbManagementService(CsvToProductListParser csvToProductListParser, ObjectMapper objectMapper,
                               OwenCategoryRepository owenCategoryRepository, ProductRepository productRepository,
                               DocRepository docRepository, DocItemRepository docItemRepository,
                               ImageRepository imageRepository, ModificationRepository modificationRepository,
                               OwenPriceRepository owenPriceRepository, KippriborMeretecPriceRepository kippriborMeyertecPriceRepository,
                               ArrivalRepository arrivalRepository, KippriborMeyrtecCategoryRepository kippriborMeyrtecCategoryRepository) {
        this.csvToProductListParser = csvToProductListParser;
        this.objectMapper = objectMapper;
        this.owenCategoryRepository = owenCategoryRepository;
        this.productRepository = productRepository;
        this.docRepository = docRepository;
        this.docItemRepository = docItemRepository;
        this.imageRepository = imageRepository;
        this.modificationRepository = modificationRepository;
        this.owenPriceRepository = owenPriceRepository;
        this.kippriborMeyertecPriceRepository = kippriborMeyertecPriceRepository;
        this.arrivalRepository = arrivalRepository;
        this.kippriborMeyrtecCategoryRepository = kippriborMeyrtecCategoryRepository;
    }

    public void reloadOwenProducts(String srcUrl) throws IOException {
        Categories categories = objectMapper.readValue(new URL(srcUrl).openStream(),
                Categories.class);
        List<OwenCategory> newCategories = new ArrayList<>(categories.getCategories());
        List<Product> newProducts = new ArrayList<>();
        List<OwenPrice> newOwenPrices = new ArrayList<>();
        List<Image> newImages = new ArrayList<>();
        List<Doc> newDocs = new ArrayList<>();
        List<DocItem> newDocItems = new ArrayList<>();

        for (OwenCategory owenCategory : categories.getCategories()) {
            for (OwenCategory subOwenCategory : owenCategory.getItems()) {
                subOwenCategory.setProducts(new HashSet<>(subOwenCategory.getProducts()).stream().toList());
                newProducts.addAll(subOwenCategory.getProducts());
                for (Product product : subOwenCategory.getProducts()) {
                    newOwenPrices.addAll(product.getOwenPrices());
                    newDocs.addAll(product.getDocs());
                    newImages.addAll(product.getImages());
                    newDocItems.addAll(product.getDocs().stream()
                            .map(Doc::getItems)
                            .flatMap(Collection::stream)
                            .toList());
                    product.setPrices(new HashSet<>(product.getOwenPrices()).stream().toList());
                }
            }
        }
        newCategories.stream().map(OwenCategory::getItems).flatMap(Collection::stream).forEach(subC -> subC.setProducts(null));

        reloadCollection(newCategories, owenCategoryRepository);
        logger.info("OwenCategories have been saved");
        reloadCollection(newProducts, productRepository);
        logger.info("OwenProducts have been saved");
        reloadCollection(newOwenPrices, owenPriceRepository);
        logger.info("OwenPrices have been saved");
        reloadCollection(newImages, imageRepository);
        logger.info("OwenImages have been saved");
        reloadCollection(newDocs, docRepository);
        logger.info("OwenDocs have been saved");
        reloadCollection(newDocItems, docItemRepository);
        logger.info("OwenDocItems have been saved");
    }

    public <T> void reloadCollection(List<T> newObjects, JpaRepository<T, ?> jpaRepository) {
        List<T> oldObjects = jpaRepository.findAll();
        Set<T> newObjectsSet = new HashSet<>(newObjects);
        List<T> forDelete = oldObjects.stream().filter(obj -> !newObjectsSet.contains(obj)).toList();
        jpaRepository.deleteAll(forDelete);
        jpaRepository.saveAll(newObjectsSet);
    }

    public void saveModifications() {
        List<Modification> freshModifications = csvToProductListParser.getProducts();
        int newProductsHash = freshModifications.hashCode();

        List<Modification> oldModifications = modificationRepository.findAll();
        int oldProductsHash = oldModifications.hashCode();

        List<Modification> forDelete = new ArrayList<>();
        for (Modification modification : oldModifications) {
            if (freshModifications.stream()
                    .noneMatch(updatedEntity -> Objects.equals(updatedEntity.getPartNumber(), modification.getPartNumber()))) {
                forDelete.add(modification);
            }
        }
        modificationRepository.deleteAll(forDelete);
        List<Modification> clearList = freshModifications.stream().filter(p -> p.getPartNumber() != null).toList();
        modificationRepository.saveAll(clearList);
    }

    public void reloadKippriborAndMeyrtecProducts(String kippriborSrcUrl, String meyrtecSrcUrl) throws IOException {
        KippriborPricesAndCategories kippriborPricesAndCategories = objectMapper.readValue(new URL(kippriborSrcUrl).openStream(),
                KippriborPricesAndCategories.class);
        List<KippriborMeyrtecCategory> kipCategories = kippriborPricesAndCategories.getCategories();
        for (KippriborMeyrtecCategory owenCategory : kipCategories) {
            owenCategory.setId(ProjectConstants.KIPPRIBOR_PREFIX + owenCategory.getId());
        }

        MeyrtecPricesAndCategories meyrtecPricesAndCategories = objectMapper.readValue(new URL(meyrtecSrcUrl).openStream(),
                MeyrtecPricesAndCategories.class);
        List<KippriborMeyrtecCategory> meyrtecCategories = meyrtecPricesAndCategories.getCategories();

        for (KippriborMeyrtecCategory owenCategory : meyrtecCategories) {
            owenCategory.setId(ProjectConstants.MEYRTEC_PREFIX + owenCategory.getId());
        }
        meyrtecCategories.addAll(kipCategories);

        reloadCollection(meyrtecCategories, kippriborMeyrtecCategoryRepository);
        List<CommonPrice> commonPrices = new ArrayList<>();
        commonPrices.addAll(kippriborPricesAndCategories.getProducts());
        commonPrices.addAll(meyrtecPricesAndCategories.getProducts());
        reloadCollection(commonPrices, kippriborMeyertecPriceRepository);
    }

    public void clearDB() {
        owenCategoryRepository.deleteAll();
        kippriborMeyrtecCategoryRepository.deleteAll();
        docRepository.deleteAll();
        docItemRepository.deleteAll();
        imageRepository.deleteAll();
        modificationRepository.deleteAll();
        owenPriceRepository.deleteAll();
        productRepository.deleteAll();
        kippriborMeyertecPriceRepository.deleteAll();
        arrivalRepository.deleteAll();
    }

    private void linkPricesAndModifications() {
        List<OwenPrice> owenPrices = owenPriceRepository.findALlByModificationIsNull();
        List<CommonPrice> kippriborMeyertecPrices = kippriborMeyertecPriceRepository.findALlByModificationIsNull();
        Set<Modification> modifications = new HashSet<>(modificationRepository.findAll());

        owenPrices
                .forEach(price -> {
                    Modification modification = new Modification();
                    modification.setPartNumber(price.getIzd_code());
                    price.setModification(
                            (modifications.contains(modification) ? modification : null));
                });

        kippriborMeyertecPrices
                .forEach(price -> {
                    Modification modification = new Modification();
                    modification.setPartNumber(price.getId());
                    price.setModification(
                            (modifications.contains(modification) ? modification : null));
                });
        owenPriceRepository.saveAll(owenPrices);
        kippriborMeyertecPriceRepository.saveAll(kippriborMeyertecPrices);
    }

    public void reloadDB() throws IOException {
        reloadOwenProducts(ProjectConstants.OWEN_SRC_URL);
        logger.info("Owen products have been saved");
        reloadKippriborAndMeyrtecProducts(ProjectConstants.KIPPRIBOR_SRC_URL, ProjectConstants.MEYERTEC_SRC_URL);
        logger.info("Meyrtec and Kippribor products have been saved");
        saveModifications();
        logger.info("Modifications have been saved");
        linkPricesAndModifications();
        logger.info("Prices and modifications have been linked");
    }
}
