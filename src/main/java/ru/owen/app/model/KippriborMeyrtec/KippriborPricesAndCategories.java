package ru.owen.app.model.KippriborMeyrtec;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class KippriborPricesAndCategories {
    private List<KippriborMeyrtecCategory> categories;
    private List<KippriborPrice> products;

    public List<? extends KippriborMeyrtecPrice> getProducts() {
        return products;
    }
}
