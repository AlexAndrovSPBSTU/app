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
    private OwenCategory owenCategory;
    private List<Image> images;
    private List<OwenPrice> owenPrices;
    private List<Doc> docs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO product = (ProductDTO) o;
        return Objects.equals(id, product.id) && Objects.equals(owenCategory, product.owenCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owenCategory);
    }
}
