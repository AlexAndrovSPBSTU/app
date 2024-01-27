package ru.owen.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.Owen.Product;

import java.util.Objects;

@Entity
@Table(name = "image")
@Setter
@Getter
public class Image {
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image image = (Image) o;
        return Objects.equals(src, image.src) && Objects.equals(alt, image.alt) && Objects.equals(product, image.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, alt, product);
    }
}
