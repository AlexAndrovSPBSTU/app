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
public class OwenProductId implements Serializable {

    private String id;

    private OwenCategory owenCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwenProductId owenProductId = (OwenProductId) o;
        return Objects.equals(id, owenProductId.id) && Objects.equals(owenCategory, owenProductId.owenCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owenCategory);
    }
}
