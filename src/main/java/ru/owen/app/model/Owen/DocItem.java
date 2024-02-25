package ru.owen.app.model.Owen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "doc_item")
@Setter
public class DocItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @ManyToOne
    @JoinColumn(name = "doc_id", referencedColumnName = "doc_id")
    @JsonBackReference
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
