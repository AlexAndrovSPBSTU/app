package ru.owen.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.Owen.OwenProduct;

import java.util.Objects;

@Entity
@Table(name = "owenimage")
@Setter
@Getter
public class OwenImage {
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Column(name = "src")
    private String src;

    @Column(name = "alt")
    private String alt;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id"),
            @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    })
    @JsonBackReference
    private OwenProduct owenProduct;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OwenImage owenImage = (OwenImage) o;
        return Objects.equals(src, owenImage.src) && Objects.equals(alt, owenImage.alt) && Objects.equals(owenProduct, owenImage.owenProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, alt, owenProduct);
    }
}
