package ru.owen.app.model.CompositeIdClasses;

import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.Owen.OwenProduct;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class OwenPriceId implements Serializable {
    private String izd_code;
    private OwenProduct owenProduct;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwenPriceId owenPriceId = (OwenPriceId) o;
        return Objects.equals(izd_code, owenPriceId.izd_code) && Objects.equals(owenProduct, owenPriceId.owenProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(izd_code, owenProduct);
    }
}
