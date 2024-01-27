package ru.owen.app.model.KippriborMeyrtec;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MeyrtecPricesAndCategories {

    private List<KippriborMeyrtecCategory> categories;
    private List<MeyertecPrice> products;

    public List<? extends CommonPrice> getProducts() {
        return products;
    }
}
