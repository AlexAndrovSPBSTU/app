package ru.owen.app.model;

import lombok.*;
import ru.owen.app.model.KippriborMeyrtec.KippriborMeyrtecPrice;
import ru.owen.app.model.Owen.OwenProduct;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    List<OwenProduct> owenProducts;
    List<KippriborMeyrtecPrice> kippriborPrices;
    List<KippriborMeyrtecPrice> meyrtecPrices;
}
