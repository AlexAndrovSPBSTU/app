package ru.owen.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.owen.app.constants.ProjectConstants;
import ru.owen.app.model.KippriborMeyrtec.*;
import ru.owen.app.model.Modification;
import ru.owen.app.model.OwenImage;
import ru.owen.app.model.Owen.*;
import ru.owen.app.repository.*;
import ru.owen.app.util.XlsToProductListParser;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class DbManagementService {
    private final XlsToProductListParser csvToProductListParser;
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
    public DbManagementService(XlsToProductListParser csvToProductListParser, ObjectMapper objectMapper,
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

    // Method is not optimal, i think, but still...
    // Here is the thing. We can save all categories and then all products and fields of the products
    // will be saved automatically. But we don't delete removed items by that.
    private void reloadOwenProducts() throws IOException {
        Categories categories = objectMapper.readValue(new URL(ProjectConstants.OWEN_SRC_URL).openStream(),
                Categories.class);
        List<OwenCategory> newCategories = new ArrayList<>(categories.getCategories());
        List<OwenProduct> newOwenProducts = new ArrayList<>();
        List<OwenPrice> newOwenPrices = new ArrayList<>();
        List<OwenImage> newOwenImages = new ArrayList<>();
        List<Doc> newDocs = new ArrayList<>();
        List<DocItem> newDocItems = new ArrayList<>();

        for (OwenCategory owenCategory : categories.getCategories()) {
            if (owenCategory.getItems() != null) {
                newCategories.addAll(owenCategory.getItems());
                for (OwenCategory subOwenCategory : owenCategory.getItems()) {
                    if (subOwenCategory.getOwenProducts() != null) {
                        subOwenCategory.setOwenProducts(new HashSet<>(subOwenCategory.getOwenProducts()).stream().toList());
                        newOwenProducts.addAll(subOwenCategory.getOwenProducts());
                        for (OwenProduct owenProduct : subOwenCategory.getOwenProducts()) {
                            if (owenProduct.getOwenPrices() != null) {
                                owenProduct.setPrices(new HashSet<>(owenProduct.getOwenPrices()).stream().toList());
                                newOwenPrices.addAll(owenProduct.getOwenPrices());
                            }
                            if (owenProduct.getDocs() != null) {
                                newDocs.addAll(owenProduct.getDocs());
                                newDocItems.addAll(owenProduct.getDocs().stream()
                                        .map(Doc::getItems)
                                        .flatMap(Collection::stream)
                                        .toList());
                            }
                            if (owenProduct.getOwenImages() != null) {
                                newOwenImages.addAll(owenProduct.getOwenImages());
                            }
                        }
                    }
                }
            }
        }
        newCategories.forEach(category -> {
            category.setOwenProducts(null);
            category.setItems(null);
        });

        reloadCollection(newCategories, owenCategoryRepository);
        logger.info("OwenCategories have been saved");

        reloadCollection(newOwenProducts, productRepository);
        logger.info("OwenProducts have been saved");

        reloadCollection(newOwenPrices, owenPriceRepository);
        logger.info("OwenPrices have been saved");

        reloadCollectionWithIdGeneratedByDefault(newOwenImages, imageRepository);
        logger.info("OwenImages have been saved");

        reloadCollectionWithIdGeneratedByDefault(newDocs, docRepository);
        logger.info("OwenDocs have been saved");

        reloadCollectionWithIdGeneratedByDefault(newDocItems, docItemRepository);
        logger.info("OwenDocItems have been saved");
    }

    private <T> void reloadCollection(List<T> newObjects, JpaRepository<T, ?> jpaRepository) {
        List<T> oldObjects = jpaRepository.findAll();
        Set<T> newObjectsSet = new HashSet<>(newObjects);
        List<T> forDelete = oldObjects.stream().filter(obj -> !newObjectsSet.contains(obj)).toList();
        if (!forDelete.isEmpty()) {
            jpaRepository.deleteAll(forDelete);
        }
        if (!newObjectsSet.isEmpty()) {
            jpaRepository.saveAll(newObjectsSet);
        }
        System.out.println("For delete - " + forDelete.size());
    }


    private <T> void reloadCollectionWithIdGeneratedByDefault(List<T> newObjects, JpaRepository<T, ?> jpaRepository) {
        Set<T> oldObjectsSet = new HashSet<>(jpaRepository.findAll());
        Set<T> newObjectsSet = new HashSet<>(newObjects);
        List<T> forDelete = oldObjectsSet.stream().filter(obj -> !newObjectsSet.contains(obj)).toList();
        if (!forDelete.isEmpty()) {
            jpaRepository.deleteAll(forDelete);
        }
        System.out.println("For delete - " + forDelete.size());
        List<T> forSave = newObjectsSet.stream().filter(obj -> !oldObjectsSet.contains(obj)).toList();
        if (!forSave.isEmpty()) {
            jpaRepository.saveAll(forSave);
        }
        System.out.println("For save - " + forSave.size());
    }

    private void reloadModifications() {
//        reloadCollection(csvToProductListParser.getProducts()
//                        .stream()
//                        .filter(modification -> modification.getPartNumber() != null)
//                        .collect(Collectors.toList()),
        reloadCollection(csvToProductListParser.getProducts(), modificationRepository);
//        modificationRepository);
    }

    private void reloadKippriborAndMeyrtecProducts() throws IOException {
        KippriborPricesAndCategories kippriborPricesAndCategories =
                objectMapper.readValue(new URL(ProjectConstants.KIPPRIBOR_SRC_URL).openStream(),
                        KippriborPricesAndCategories.class);

        List<KippriborMeyrtecCategory> kipCategories = kippriborPricesAndCategories.getCategories();
        for (KippriborMeyrtecCategory kippriborMeyrtecCategory : kipCategories) {
            kippriborMeyrtecCategory.setId(ProjectConstants.KIPPRIBOR_PREFIX + kippriborMeyrtecCategory.getId());
        }

        MeyrtecPricesAndCategories meyrtecPricesAndCategories = objectMapper.
                readValue(new URL(ProjectConstants.MEYERTEC_SRC_URL).openStream(),
                        MeyrtecPricesAndCategories.class);
        List<KippriborMeyrtecCategory> meyrtecCategories = meyrtecPricesAndCategories.getCategories();

        for (KippriborMeyrtecCategory kippriborMeyrtecCategory : meyrtecCategories) {
            kippriborMeyrtecCategory.setId(ProjectConstants.MEYRTEC_PREFIX + kippriborMeyrtecCategory.getId());
        }

        List<KippriborMeyrtecCategory> allCategories = new ArrayList<>();
        allCategories.addAll(kipCategories);
        allCategories.addAll(meyrtecCategories);
        reloadCollection(allCategories, kippriborMeyrtecCategoryRepository);
        logger.info("KippriborMeyrtecCategories have been saved");

        List<KippriborMeyrtecPrice> allPrices = new ArrayList<>();
        allPrices.addAll(kippriborPricesAndCategories.getProducts());
        allPrices.addAll(meyrtecPricesAndCategories.getProducts());
        reloadCollection(allPrices, kippriborMeyertecPriceRepository);
        logger.info("KippriborMeyrtecPrices have been saved");

        List<Arrival> allArrivals = new ArrayList<>(allPrices.stream()
                .map(KippriborMeyrtecPrice::getArrivals).flatMap(Collection::stream).toList());
        reloadCollectionWithIdGeneratedByDefault(allArrivals, arrivalRepository);
        logger.info("Arrivals have been saved");

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
        List<OwenPrice> owenPrices = owenPriceRepository.findAllByModificationIsNull();
        List<KippriborMeyrtecPrice> kippriborMeyertecPrices = kippriborMeyertecPriceRepository.findAllByModificationIsNull();
        Set<String> partNumbers = modificationRepository.findAll().stream().map(Modification::getPartNumber).collect(Collectors.toSet());

        owenPrices
                .forEach(price -> price.setModification(
                        (partNumbers.contains(price.getIzd_code()) ? Modification.builder().partNumber(price.getIzd_code()).build() : null)));
        kippriborMeyertecPrices
                .forEach(price -> price.setModification(
                        (partNumbers.contains(price.getId()) ? Modification.builder().partNumber(price.getId()).build() : null)));

        owenPriceRepository.saveAll(owenPrices);
        kippriborMeyertecPriceRepository.saveAll(kippriborMeyertecPrices);
    }

    public void reloadDB() throws IOException {
        reloadOwenProducts();
        logger.info("Owen has been saved");
        reloadKippriborAndMeyrtecProducts();
        logger.info("Meyrtec and Kippribor products have been saved");
        reloadModifications();
        logger.info("Modifications have been saved");
        linkPricesAndModifications();
        logger.info("Prices and modifications have been linked");
    }
}
