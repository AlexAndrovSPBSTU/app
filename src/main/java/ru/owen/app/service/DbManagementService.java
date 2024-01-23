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
import java.util.stream.Collectors;

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

    private void reloadOwenProducts() throws IOException {
        Categories categories = objectMapper.readValue(new URL(ProjectConstants.OWEN_SRC_URL).openStream(),
                Categories.class);
        List<OwenCategory> newCategories = new ArrayList<>(categories.getCategories());
        List<Product> newProducts = new ArrayList<>();
        List<OwenPrice> newOwenPrices = new ArrayList<>();
        List<Image> newImages = new ArrayList<>();
        List<Doc> newDocs = new ArrayList<>();
        List<DocItem> newDocItems = new ArrayList<>();

        for (OwenCategory owenCategory : categories.getCategories()) {
            newCategories.addAll(owenCategory.getItems());
            for (OwenCategory subOwenCategory : owenCategory.getItems()) {
                subOwenCategory.setProducts(new HashSet<>(subOwenCategory.getProducts()).stream().toList());
                newProducts.addAll(subOwenCategory.getProducts());
                for (Product product : subOwenCategory.getProducts()) {
                    product.setPrices(new HashSet<>(product.getOwenPrices()).stream().toList());
                    newOwenPrices.addAll(product.getOwenPrices());
                    newDocs.addAll(product.getDocs());
                    newImages.addAll(product.getImages());
                    newDocItems.addAll(product.getDocs().stream()
                            .map(Doc::getItems)
                            .flatMap(Collection::stream)
                            .toList());
                }
            }
        }
        newCategories.forEach(category -> {
            category.setProducts(null);
            category.setItems(null);
        });

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

    private <T> void reloadCollection(List<T> newObjects, JpaRepository<T, ?> jpaRepository) {
        List<T> oldObjects = jpaRepository.findAll();
        Set<T> newObjectsSet = new HashSet<>(newObjects);
        List<T> forDelete = oldObjects.stream().filter(obj -> !newObjectsSet.contains(obj)).toList();
        jpaRepository.deleteAll(forDelete);
        jpaRepository.saveAll(newObjectsSet);
        System.out.println("For delete - " + forDelete.size());
    }

    private void reloadModifications() {
        reloadCollection(csvToProductListParser.getProducts()
                        .stream()
                        .filter(modification -> modification.getPartNumber() != null)
                        .collect(Collectors.toList()),
                modificationRepository);
    }

    private void reloadKippriborAndMeyrtecProducts() throws IOException {
        KippriborPricesAndCategories kippriborPricesAndCategories = objectMapper.readValue(new URL(ProjectConstants.KIPPRIBOR_SRC_URL).openStream(),
                KippriborPricesAndCategories.class);

        List<KippriborMeyrtecCategory> kipCategories = kippriborPricesAndCategories.getCategories();
        for (KippriborMeyrtecCategory kippriborMeyrtecCategory : kipCategories) {
            kippriborMeyrtecCategory.setId(ProjectConstants.KIPPRIBOR_PREFIX + kippriborMeyrtecCategory.getId());
        }

        MeyrtecPricesAndCategories meyrtecPricesAndCategories = objectMapper.readValue(new URL(ProjectConstants.MEYERTEC_SRC_URL).openStream(),
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

        List<CommonPrice> allPrices = new ArrayList<>();
        allPrices.addAll(kippriborPricesAndCategories.getProducts());
        allPrices.addAll(meyrtecPricesAndCategories.getProducts());
        reloadCollection(allPrices, kippriborMeyertecPriceRepository);
        logger.info("KippriborMeyrtecPrices have been saved");

        List<Arrival> allArrivals = new ArrayList<>(allPrices.stream().map(CommonPrice::getArrivals).flatMap(Collection::stream).toList());
        reloadCollection(allArrivals, arrivalRepository);
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
        List<CommonPrice> kippriborMeyertecPrices = kippriborMeyertecPriceRepository.findAllByModificationIsNull();
        Set<Modification> modifications = new HashSet<>(modificationRepository.findAll());

        owenPrices
                .forEach(price -> {
                    Modification modification = Modification.builder().partNumber(price.getIzd_code()).build();
                    price.setModification(
                            (modifications.contains(modification) ? modification : null));
                });
        kippriborMeyertecPrices
                .forEach(price -> {
                    Modification modification = Modification.builder().partNumber(price.getId()).build();
                    price.setModification(
                            (modifications.contains(modification) ? modification : null));
                });

        owenPriceRepository.saveAll(owenPrices);
        kippriborMeyertecPriceRepository.saveAll(kippriborMeyertecPrices);
    }

    public void reloadDB() throws IOException {
        reloadOwenProducts();
        logger.info("Owen products have been saved");
        reloadKippriborAndMeyrtecProducts();
        logger.info("Meyrtec and Kippribor products have been saved");
        reloadModifications();
        logger.info("Modifications have been saved");
        linkPricesAndModifications();
        logger.info("Prices and modifications have been linked");
    }
}
