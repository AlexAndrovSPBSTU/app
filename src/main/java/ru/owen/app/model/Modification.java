package ru.owen.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import ru.owen.app.model.KippriborMeyrtec.CommonPrice;
import ru.owen.app.model.Owen.OwenPrice;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "modification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"owenPrices", "kippriborMeyertecPrices"})
@Getter
@Setter
public class Modification {
    @Id
    @Column(name = "part_number")
    //sku
    private String partNumber;

    @Column(name = "working_title")
    //Наименование рабочее
    private String workingTitle;

    @Column(name = "modification")
    private String modification;

    @Column(name = "full_title")
    //Наименование полное
    private String fullTitle;

    @Column(name = "price")
    //Цена
    private Double price_;

    @Column(name = "price_NDS")
    //Цена с ндс
    private Double priceNDS;

    @Column(name = "product_serial")
    //Серийность товара
    private String productSerial;

    @Column(name = "group_")
    //Группа товара
    private String group;

    @Column(name = "delivery_time")
    //Срок поставки
    private String deliveryTime;

    @Column(name = "size")
    //Крупность
    private Integer size;

    @Column(name = "oversize")
    //Гиперкрупность
    private Integer oversize;

    @Column(name = "multiplicity")
    //Кратность
    private Integer multiplicity;

    @Column(name = "code_TNVED")
    private String codeTNVED;

    @Column(name = "status")
    //Статус товара
    private String status;

    @Column(name = "guarantee_period")
    //Гарантийный срок
    private Short guaranteePeriod;

    @Column(name = "market_exit_date")
    //Дата вывода с рынка
    private String MarketExitDate;

    @OneToMany(mappedBy = "modification")
    private List<OwenPrice> owenPrices;

    @OneToMany(mappedBy = "modification")
    @JsonIgnore
    private List<CommonPrice> kippriborMeyertecPrices;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Modification that = (Modification) o;
        return Objects.equals(partNumber, that.partNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partNumber);
    }

    public String toOrderItem(int num, int amount) {
        return "[" + num + "~" + fullTitle + "~" + amount + "~" + priceNDS + "~" + priceNDS * amount + "~" + deliveryTime + "]";
    }
}
