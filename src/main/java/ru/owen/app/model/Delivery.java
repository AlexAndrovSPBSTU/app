package ru.owen.app.model;

import lombok.Getter;

@Getter
public enum Delivery {
    PICKUP_MOSCOW(true, "Самовывоз - Москва, 1-ая ул. Энтузиастов 4, оф. 4", 0.0),
    PICKUP_PSKOV(true, "Самовывоз - Псков, улица Советская , дом 49, офис 1010", 0.0),
    PICKUP_SAINT_PETERSBURG(true, "Самовывоз - Санкт-Петербург, проспект Стачек 37, оф. 108", 0.0),

    DELIVERY_PAY(false, " (оплата при получении)", 0.0),
    DELIVERY_NOT_PAY(false, " (фиксированная стоимость доставки - 1000р)", 1000.0);


    Delivery(boolean pickup, String address, double deliveryPrice) {
        this.pickup = pickup;
        this.address = address;
        this.deliveryPrice = deliveryPrice;
    }

    private final boolean pickup;
    private final String address;
    private final double deliveryPrice;

    private String deliveryAddress;
    private final static String prefix = "До ближайшего пункта выдачи по адресу - ";

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getFullAddress() {
        if (deliveryAddress != null) {
            return prefix + deliveryAddress + address;
        } else {
            return address;
        }
    }
}
