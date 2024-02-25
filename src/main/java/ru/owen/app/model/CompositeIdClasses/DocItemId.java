package ru.owen.app.model.CompositeIdClasses;

import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.Owen.Doc;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class DocItemId implements Serializable {
    private String name;
    private String link;
    private Doc doc;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocItemId docItemId = (DocItemId) o;
        return Objects.equals(name, docItemId.name) && Objects.equals(link, docItemId.link) && Objects.equals(doc, docItemId.doc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, link, doc);
    }
}
