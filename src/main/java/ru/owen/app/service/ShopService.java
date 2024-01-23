package ru.owen.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.owen.app.model.Customer;
import ru.owen.app.model.Modification;
import ru.owen.app.model.cart.CartItem;
import ru.owen.app.model.cart.CartItemId;
import ru.owen.app.model.cart.CartResponse;
import ru.owen.app.repository.CartItemRepository;
import ru.owen.app.repository.CustomerRepository;
import ru.owen.app.repository.ModificationRepository;
import ru.owen.app.util.Dart;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ModificationRepository modificationRepository;
    private final Dart dart;

    @Autowired
    public ShopService(CartItemRepository cartItemRepository, CustomerRepository customerRepository, ModificationRepository modificationRepository, Dart dart) {
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.modificationRepository = modificationRepository;
        this.dart = dart;
    }

    public List<CartResponse> getCartItems() {
        return cartItemRepository.findAllByCustomer(getCustomer()).stream()
                .map(cartItem -> {
                            Modification modification = cartItem.getModification();
                            return CartResponse.builder()
                                    .part_number(modification.getPartNumber())
                                    .price(modification.getPriceNDS())
                                    .fullTitle(modification.getFullTitle())
                                    .totalAmount(cartItem.getTotalCount())
                                    .build();
                        }
                ).collect(Collectors.toList());
    }

    public void addProductToCart(String partNumber) {
        Modification modification = modificationRepository.findById(partNumber).get();
        Customer customer = getCustomer();
        CartItem cartItem =
                cartItemRepository.findById(CartItemId.builder()
                                .customer(customer)
                                .modification(modification)
                                .build())
                        .orElse(CartItem.builder()
                                .totalCount(0)
                                .modification(modification)
                                .customer(customer)
                                .build()
                        );
        cartItem.increaseTotalCount();
        cartItemRepository.save(cartItem);
    }

    public void reduce(String partNumber) {
        Customer customer = getCustomer();
        CartItem cartItem = cartItemRepository.findById(CartItemId
                .builder()
                .customer(customer)
                .modification(Modification.builder().partNumber(partNumber).build())
                .build()).get();
        if (cartItem.getTotalCount() == cartItem.getModification().getMultiplicity()) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.reduceTotalCount();
            cartItemRepository.save(cartItem);
        }
    }

    public void delete(String partNumber) {
        Customer customer = getCustomer();
        cartItemRepository.deleteById(CartItemId
                .builder()
                .customer(customer)
                .modification(Modification.builder().partNumber(partNumber).build())
                .build());
    }

    private Customer getCustomer() {
        return customerRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).get();
    }

    public void createPriceList() {
        Double itogo = 0.0;
        Double nds = 0.0;
        StringBuilder productList = new StringBuilder();
        collectProducts(itogo, nds, productList);
        dart.createPriceList("priceList", String.valueOf(itogo), String.valueOf(nds), productList.toString());
    }

    public void createInvoice(String companyData, String deliveryAddressValue, String deliveryPriceValue, String couponValue) {
        Double itogo = 0.0;
        Double nds = 0.0;
        StringBuilder productList = new StringBuilder();
        collectProducts(itogo, nds, productList);
        dart.createInvoice("invoice", companyData, deliveryAddressValue, deliveryPriceValue,
                String.valueOf(itogo), String.valueOf(nds), couponValue, productList.toString());
    }

    private void collectProducts(Double itogo, Double nds, StringBuilder productList) {
        List<CartItem> cartItems = cartItemRepository.findAllByCustomer(getCustomer());
        productList.append("[");
        int num = 1;
        for (CartItem cartItem : cartItems) {
            nds += (cartItem.getModification().getPriceNDS() - cartItem.getModification().getPrice_()) * cartItem.getTotalCount();
            itogo += cartItem.getModification().getPriceNDS();
            productList.append(cartItem.getModification().toOrderItem(num++, cartItem.getTotalCount())).append(",");
        }
        productList.deleteCharAt(productList.length() - 1);
        productList.append("]");
    }

}
