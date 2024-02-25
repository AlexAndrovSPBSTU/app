package ru.owen.app.model.Owen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.CompositeIdClasses.DocId;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "doc")
@IdClass(DocId.class)
public class Doc {

    @Id
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id"),
            @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    })
    @JsonBackReference
    @Id
    private OwenProduct owenProduct;

    @OneToMany(mappedBy = "doc", cascade = CascadeType.ALL)
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
        return Objects.equals(name, doc.name) && Objects.equals(owenProduct, doc.owenProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owenProduct);
    }
}

