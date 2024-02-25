package ru.owen.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.owen.app.model.Mutual.Delivery;
import ru.owen.app.model.Mutual.InvoiceRequest;
import ru.owen.app.model.Cart.CartResponse;
import ru.owen.app.service.ShopService;

import java.io.File;
import java.util.List;

@RestController
public class ShopController {

    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/cart")
    public List<CartResponse> getCart() {
        return shopService.getCartItems();
    }

    @PatchMapping("/addToCart")
    public ResponseEntity<?> addProductToCart(@RequestParam String partNumber) {
        shopService.addProductToCart(partNumber);
        return ResponseEntity.ok("Product has been added to the cart");
    }

    @PatchMapping("/reduce")
    public ResponseEntity<?> reduce(@RequestParam String partNumber) {
        shopService.reduce(partNumber);
        return ResponseEntity.ok("Product has been reduced");
    }

    @DeleteMapping("/deleteCardItem")
    public ResponseEntity<?> delete(@RequestParam String partNumber) {
        shopService.delete(partNumber);
        return ResponseEntity.ok("Product has been deleted from the cart");
    }

    @GetMapping("/invoices")
    public ResponseEntity<?> getInvoices() {
        return ResponseEntity.ok(shopService.getInvoices());
    }


    //1 -    "Самовывоз - Москва, 1-ая ул. Энтузиастов 4, оф. 4"
//2 -    "Самовывоз - Псков, улица Советская , дом 49, офис 1010"
//3 -    "Самовывоз - Санкт-Петербург, проспект Стачек 37, оф. 108"
//4 -    (оплата при получении) + address
//5 -    (фиксированная стоимость доставки - 1000р) + address
    @PostMapping("/invoice")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        Delivery delivery = invoiceRequest.getDelivery();
        if (invoiceRequest.getDeliveryAddress() != null) {
            delivery.setDeliveryAddress(invoiceRequest.getDeliveryAddress());
        }
        String filePath = shopService.createInvoice(invoiceRequest.getCompanyData(), delivery,
                invoiceRequest.getCoupon());

        File pdfFile = new File(filePath);

        if (!pdfFile.exists()) {
            // Обработайте ситуацию, если файл не найден
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "document.pdf");

        // Создайте ресурс из файла
        Resource resource = new FileSystemResource(pdfFile);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

    }

    @PostMapping("/price-list")
    public ResponseEntity<?> createPriceLists() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "document.pdf");
        String filePath = shopService.createPriceList();
        File priceList = new File(filePath);
        // Создайте ресурс из файла
        Resource resource = new FileSystemResource(priceList);


        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
