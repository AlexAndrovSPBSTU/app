package ru.owen.app.model.Owen;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.CompositeIdClasses.ProductId;
import ru.owen.app.model.Image;

import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "product")
@IdClass(ProductId.class)
public class Product {
    @Id
    @Column(name = "product_id")
    @Setter
    private String id;

    @Column(name = "name")
    @Setter
    private String name;

    @Setter
    private String link;

    @Setter
    private String sku;

    @Setter
    private String image;

    @Setter
    private String thumb;

    @Setter
    @JsonProperty("desc")
    private String description;

    @Setter
    private String specs;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @Id
    @JsonBackReference
    @Setter
    private OwenCategory owenCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference("product-images")
    private List<Image> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OwenPrice> owenPrices;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference("product-docs")
    private List<Doc> docs;


    @JsonSetter
    public void setDocs(List<Doc> docs) {
        this.docs = docs;
        if (this.docs != null) {
            this.docs.forEach(doc -> doc.setProduct(this));
        }
    }

    @JsonSetter
    public void setPrices(List<OwenPrice> owenPrices) {
        this.owenPrices = owenPrices;
        if (this.owenPrices != null) {
            this.owenPrices.forEach(price -> price.setProduct(this));
        }
    }

    @JsonSetter
    public void setImages(List<Image> images) {
        this.images = images;
        if (this.images != null) {
            this.images.forEach(image -> image.setProduct(this));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(owenCategory, product.owenCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owenCategory);
    }

}

