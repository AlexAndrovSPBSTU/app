package ru.owen.app.model.Cart;

import lombok.*;
import ru.owen.app.model.Customer;
import ru.owen.app.model.Modification;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemId implements Serializable {
    private Customer customer;
    private Modification modification;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemId that = (CartItemId) o;
        return Objects.equals(customer, that.customer) && Objects.equals(modification, that.modification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, modification);
    }
}
