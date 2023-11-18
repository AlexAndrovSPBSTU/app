package ru.owen.app.model.CompositeIdClasses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.model.KippriborMeyrtec.CommonPrice;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class ArrivalId implements Serializable {

    private Integer quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date date;

    private CommonPrice price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrivalId arrivalId = (ArrivalId) o;
        return Objects.equals(quantity, arrivalId.quantity) && Objects.equals(date, arrivalId.date) && Objects.equals(price, arrivalId.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, date, price);
    }
}
