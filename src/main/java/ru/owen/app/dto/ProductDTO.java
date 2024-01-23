package ru.owen.app.dto;

import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.Image;
import ru.owen.app.model.Owen.Doc;
import ru.owen.app.model.Owen.OwenCategory;
import ru.owen.app.model.Owen.OwenPrice;

import java.util.List;
import java.util.Objects;

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
    private List<Image> images;
    private List<OwenPrice> owenPrices;
    private List<Doc> docs;
}
