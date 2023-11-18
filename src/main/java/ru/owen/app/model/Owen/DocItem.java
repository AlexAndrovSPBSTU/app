package ru.owen.app.model.Owen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "doc_item")
@Setter
public class DocItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @ManyToOne
    @JoinColumn(name = "doc_id", referencedColumnName = "doc_id")
    @JsonBackReference("doc-doc_items")
    private Doc doc;

}
