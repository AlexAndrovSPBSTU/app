package ru.owen.app.model.Owen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name = "doc")
public class Doc {
    @Id
    @Column(name = "doc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private int id;

    @Column(name = "name")
    @Setter
    private String name;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id"),
            @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    })
    @JsonBackReference("product-docs")
    @Setter
    private Product product;

    @OneToMany(mappedBy = "doc", cascade = CascadeType.ALL)
    @JsonManagedReference("doc-doc_items")
    private List<DocItem> items;

    @JsonSetter
    public void setItems(List<DocItem> items) {
        this.items = items;
        if (this.items != null) {
            this.items.forEach(i -> i.setDoc(this));
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
        Doc doc = (Doc) o;
        return Objects.equals(name, doc.name) && Objects.equals(product, doc.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, product);
    }
}

