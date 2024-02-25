package ru.owen.app.model.Mutual;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceRequest {
    private String name;
    private String address;
    @JsonProperty(value = "INN")
    private String INN;
    @JsonProperty(value = "KPP")
    private String KPP;
    private String deliveryAddress;
    private byte coupon;
    private Delivery delivery;

    public String getCompanyData() {
        return name + ", " + INN + ", " + KPP + ", " + address;
    }
}



