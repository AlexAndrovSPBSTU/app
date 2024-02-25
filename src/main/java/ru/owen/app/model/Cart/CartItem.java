package ru.owen.app.model.Cart;

import jakarta.persistence.*;
import lombok.*;
import ru.owen.app.model.Mutual.Customer;
import ru.owen.app.model.Mutual.Modification;


@Getter
@Setter
@Entity
@Table(name = "cart_item")
@IdClass(CartItemId.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @ManyToOne
    @JoinColumn(name = "modification_id", referencedColumnName = "part_number")
    private Modification modification;

    @Column(name = "total_count")
    private int totalCount;

    @Id
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    public void increaseTotalCount() {
        this.totalCount = this.totalCount + modification.getMultiplicity();
    }

    public void reduceTotalCount() {
        this.totalCount = this.totalCount - modification.getMultiplicity();
    }
}
