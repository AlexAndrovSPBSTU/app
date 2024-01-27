package ru.owen.app.model.Owen;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Builder
@Table(name = "owencategory")
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OwenCategory {
    @Id
    @Column(name = "category_id")
    @Setter
    private String id;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "link")
    @Setter
    private String link;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", referencedColumnName = "category_id")
    @Setter
    private OwenCategory parent;

    @OneToMany(mappedBy = "owenCategory")
    private List<Product> products;

    @OneToMany(mappedBy = "parent")
    private List<OwenCategory> items;


    @JsonSetter
    public void setItems(List<OwenCategory> items) {
        this.items = items;
        if (this.items != null) {
            for (OwenCategory child : this.items) {
                child.setParent(this);
            }
        }
    }


    @JsonSetter
    public void setProducts(List<Product> products) {
        this.products = products;
        if (this.products != null) {
            for (Product product : this.products) {
                product.setOwenCategory(this);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwenCategory owenCategory = (OwenCategory) o;
        return Objects.equals(id, owenCategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
