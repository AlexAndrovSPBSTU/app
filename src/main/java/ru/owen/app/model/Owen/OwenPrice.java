package ru.owen.app.model.Owen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.CompositeIdClasses.PriceId;
import ru.owen.app.model.Modification;

import java.util.Objects;


@Entity
@Table(name = "owenprice")
@IdClass(PriceId.class)
@Getter
@Setter
public class OwenPrice {
    @Id
    @Column(name = "izd_code")
    private String izd_code;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id"),
            @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    })
    @Id
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "modification", referencedColumnName = "part_number")
    private Modification modification;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwenPrice owenPrice = (OwenPrice) o;
        return Objects.equals(izd_code, owenPrice.izd_code) && Objects.equals(product, owenPrice.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(izd_code, product);
    }
}
