package ru.owen.app.model;

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
    private String deliveryPrice;
    private String coupon;

    public String getCompanyData() {
        return name + ", " + INN + ", " + KPP + ", " + address;
    }
}
