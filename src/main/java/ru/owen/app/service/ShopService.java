package ru.owen.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.owen.app.model.Customer;
import ru.owen.app.model.Delivery;
import ru.owen.app.model.Modification;
import ru.owen.app.model.Cart.CartItem;
import ru.owen.app.model.Cart.CartItemId;
import ru.owen.app.model.Cart.CartResponse;
import ru.owen.app.repository.CartItemRepository;
import ru.owen.app.repository.CustomerRepository;
import ru.owen.app.repository.InvoiceRepository;
import ru.owen.app.repository.ModificationRepository;
import ru.owen.app.util.InvoiceAndPriceListGenerator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ShopService {
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ModificationRepository modificationRepository;
    private final InvoiceAndPriceListGenerator invoiceAndPriceListGenerator;

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public ShopService(CartItemRepository cartItemRepository, CustomerRepository customerRepository, ModificationRepository modificationRepository, InvoiceAndPriceListGenerator invoiceAndPriceListGenerator, InvoiceRepository invoiceRepository) {
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.modificationRepository = modificationRepository;
        this.invoiceAndPriceListGenerator = invoiceAndPriceListGenerator;
        this.invoiceRepository = invoiceRepository;
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

    public String createPriceList() {
        double[] itogo = {0.0};
        StringBuilder productList = new StringBuilder();
        collectProducts(itogo, productList);

        String itogoRounded = String.format("%.2f", itogo[0]).replace('.', ',');
        String ndsRounded = String.format("%.2f", itogo[0] / 6).replace('.', ',');

        return invoiceAndPriceListGenerator.createPriceList(getCustomer().getId(), "price-list", itogoRounded, ndsRounded, productList.toString());
    }

    public String createInvoice(String companyData, Delivery delivery, byte couponValue) {
        double[] itogo = {0.0};
        StringBuilder productList = new StringBuilder();
        collectProducts(itogo, productList);

        itogo[0] *= (double) (100 - couponValue) / 100;
        itogo[0] += delivery.getDeliveryPrice();

        String itogoRounded = String.format("%.2f", itogo[0]).replace('.', ',');
        String ndsRounded = String.format("%.2f", itogo[0] / 6).replace('.', ',');
        String deliveryPrice = String.format("%.2f", delivery.getDeliveryPrice()).replace('.', ',');

//        String invoiceNumber = generateRandomString();
//
//        while (invoiceRepository.existsById(invoiceNumber)) {
//            invoiceNumber = generateRandomString();
//        }

//        invoiceRepository.save(Invoice.builder().number(invoiceNumber).customer(getCustomer()).build());

        return invoiceAndPriceListGenerator.createInvoice(getCustomer().getId(), "invoice", companyData, delivery.getFullAddress(), deliveryPrice,
                itogoRounded, ndsRounded, String.valueOf(couponValue), productList.toString());

    }

    private void collectProducts(double[] itogo, StringBuilder productList) {
        List<CartItem> cartItems = cartItemRepository.findAllByCustomer(getCustomer());
        productList.append("[");
        int num = 1;
        for (CartItem cartItem : cartItems) {
            itogo[0] += cartItem.getModification().getPriceNDS() * cartItem.getTotalCount();
            productList.append(cartItem.getModification().toOrderItem(num++, cartItem.getTotalCount())).append(",");
        }
        productList.deleteCharAt(productList.length() - 1);
        productList.append("]");
    }


    public static String generateRandomString() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            int randomDigit = random.nextInt(10); // Generates a random digit (0-9)
            stringBuilder.append(randomDigit);
        }

        return stringBuilder.toString();
    }

    public List<String> getInvoices() {
        return invoiceRepository.findAllByCustomer(getCustomer()).stream().map(invoice -> invoice.getNumber() + ".pdf").collect(Collectors.toList());
    }
}
