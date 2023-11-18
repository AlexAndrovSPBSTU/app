package ru.owen.app.model.KippriborMeyrtec;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
public class KippriborPricesAndCategories {
    @Getter
    private List<KippriborMeyrtecCategory> categories;
    private List<KippriborPrice> products;

    public List<? extends CommonPrice> getProducts() {
        return products;
    }
}
