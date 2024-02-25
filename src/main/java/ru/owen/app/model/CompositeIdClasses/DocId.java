package ru.owen.app.model.CompositeIdClasses;


import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.Owen.OwenProduct;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class DocId implements Serializable {
    private String name;
    private OwenProduct owenProduct;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocId docId = (DocId) o;
        return Objects.equals(name, docId.name) && Objects.equals(owenProduct, docId.owenProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owenProduct);
    }
}
