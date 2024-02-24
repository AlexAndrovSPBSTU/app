package ru.owen.app.model.KippriborMeyrtec;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import ru.owen.app.model.Modification;

import java.util.List;
import java.util.Objects;

@Getter
@Table(name = "kippribormeyrtecprice")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type",
        discriminatorType = DiscriminatorType.INTEGER)
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KippriborMeyrtecPrice {
    @Id
    @Column(name = "id")
    private String id;
    private String name;
    private String fullName;
    private Integer multiplicity;
    private String unit;
    private Double price;
    private String text;
    private String text2;
    private String text3;
    private String packing;
    private String additionalIds;
    private String analogIds;
    private String storeStatus;
    private Integer storeValue;
    @Column(insertable=false, updatable=false)
    private byte type;

    @ManyToOne
    @JoinColumn(name = "modification", referencedColumnName = "part_number")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Modification modification;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected KippriborMeyrtecCategory category;

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL)
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Arrival> arrivals;

    @JsonSetter
    public void setArrivals(List<Arrival> arrivals) {
        this.arrivals = arrivals;
        if (this.arrivals != null) {
            this.arrivals.forEach(a -> a.setPrice(this));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KippriborMeyrtecPrice that = (KippriborMeyrtecPrice) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CommonPrice{" +
                "id='" + id + '\'' +
                '}';
    }
}
