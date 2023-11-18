package ru.owen.app.model.CompositeIdClasses;

import lombok.*;
import ru.owen.app.model.Owen.OwenCategory;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductId implements Serializable {

    private String id;

    private OwenCategory owenCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return Objects.equals(id, productId.id) && Objects.equals(owenCategory, productId.owenCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owenCategory);
    }
}
