package ru.owen.app.model.KippriborMeyrtec;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.*;
import ru.owen.app.model.Modification;

import java.util.List;

@Getter
@Table(name = "kippribormeyrtecprice")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "price_type",
        discriminatorType = DiscriminatorType.INTEGER)
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonPrice {
    @Id
    @Column(name = "id")
    private String id;
    private Integer multiplicity;
    private String unit;
    private String fullName;
    private Double price;
    private String text;
    private String text2;
    private String text3;
    private String packing;
    private String additionalIds;
    private String analogIds;
    private String storeStatus;
    private Integer storeValue;
    private String name;

    @ManyToOne
    @JoinColumn(name = "modification", referencedColumnName = "part_number")
    @JsonBackReference("modification-prices")
    private Modification modification;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @JsonBackReference("category-prices")
    protected KippriborMeyrtecCategory category;

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL)
    @JsonManagedReference("price-arrivals")
    private List<Arrival> arrivals;

    @JsonSetter
    public void setArrivals(List<Arrival> arrivals) {
        this.arrivals = arrivals;
        if (this.arrivals != null) {
            this.arrivals.forEach(a -> a.setPrice(this));
        }
    }
}
