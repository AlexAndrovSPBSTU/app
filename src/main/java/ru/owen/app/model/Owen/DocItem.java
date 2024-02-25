package ru.owen.app.model.Owen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.CompositeIdClasses.DocItemId;

import java.util.Objects;

@Getter
@Entity
@Table(name = "doc_item")
@Setter
@IdClass(DocItemId.class)
public class DocItem {

    @Id
    @Column(name = "name_")
    private String name;

    @Id
    @Column(name = "link")
    private String link;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "name", referencedColumnName = "name"),
            @JoinColumn(name = "product_id", referencedColumnName = "product_id"),
            @JoinColumn(name = "category_id", referencedColumnName = "category_id")})
    @JsonBackReference
    @Id
    private Doc doc;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocItem docItem = (DocItem) o;
        return Objects.equals(name, docItem.name) && Objects.equals(link, docItem.link) && Objects.equals(doc, docItem.doc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, link, doc);
    }
}
