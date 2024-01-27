package ru.owen.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.owen.app.model.Delivery;
import ru.owen.app.model.InvoiceRequest;
import ru.owen.app.model.cart.CartResponse;
import ru.owen.app.model.CompanyData;
import ru.owen.app.service.ShopService;

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
        shopService.createInvoice(invoiceRequest.getCompanyData(), delivery ,
                invoiceRequest.getCoupon());
        return ResponseEntity.ok("Invoice has been created");
    }

    @PostMapping("/price-list")
    public ResponseEntity<?> createPriceLists() {
        shopService.createPriceList();
        return ResponseEntity.ok("Price list has been created");
    }
}
