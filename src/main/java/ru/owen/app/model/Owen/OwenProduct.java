package ru.owen.app.model.Owen;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.CompositeIdClasses.OwenProductId;
import ru.owen.app.model.Mutual.OwenImage;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "owenproduct")
@IdClass(OwenProductId.class)
public class OwenProduct {
    @Id
    @Column(name = "product_id")
    private String id;

    @Column(name = "name")
    private String name;

    private String link;

    private String sku;

    private String image;

    private String thumb;

    @JsonProperty("desc")
    private String description;

    private String specs;

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @JsonBackReference
    private OwenCategory owenCategory;

    @OneToMany(mappedBy = "owenProduct")
    @JsonProperty(value = "images")
    private List<OwenImage> owenImages;

    @OneToMany(mappedBy = "owenProduct", cascade = CascadeType.ALL)
    private List<OwenPrice> owenPrices;

    @OneToMany(mappedBy = "owenProduct")
    private List<Doc> docs;


    @JsonSetter
    public void setDocs(List<Doc> docs) {
        this.docs = docs;
        if (this.docs != null) {
            this.docs.forEach(doc -> doc.setOwenProduct(this));
        }
    }

    @JsonSetter
    public void setPrices(List<OwenPrice> owenPrices) {
        this.owenPrices = owenPrices;
        if (this.owenPrices != null) {
            this.owenPrices.forEach(price -> price.setOwenProduct(this));
        }
    }

    @JsonSetter
    public void setOwenImages(List<OwenImage> owenImages) {
        this.owenImages = owenImages;
        if (this.owenImages != null) {
            this.owenImages.forEach(image -> image.setOwenProduct(this));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OwenProduct owenProduct = (OwenProduct) o;
        return Objects.equals(id, owenProduct.id) && Objects.equals(owenCategory, owenProduct.owenCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owenCategory);
    }

}

