package ru.owen.app.dto;

import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.OwenImage;
import ru.owen.app.model.Owen.Doc;
import ru.owen.app.model.Owen.OwenPrice;

import java.util.List;

@Getter
@Setter
public class ProductDTO {
    private String id;
    private String name;
    private String link;
    private String sku;
    private String image;
    private String thumb;
    private String description;
    private String specs;
    private List<OwenImage> owenImages;
    private List<Doc> docs;
    private List<OwenPrice> owenPrices;
}
