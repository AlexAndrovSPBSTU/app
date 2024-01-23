package ru.owen.app.model.KippriborMeyrtec;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.CompositeIdClasses.ArrivalId;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "arrival")
@IdClass(ArrivalId.class)
public class Arrival {
    @Id
    @Column(name = "quantity")
    private Integer quantity;

    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @Column(name = "date")
    private Date date;

    @Id
    @ManyToOne
    @JoinColumn(name = "price_id", referencedColumnName = "id")
    @JsonBackReference("price-arrivals")
    private CommonPrice price;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Arrival arrival = (Arrival) o;
        return Objects.equals(quantity, arrival.quantity) && Objects.equals(date, arrival.date) && Objects.equals(price, arrival.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, date, price);
    }
}
