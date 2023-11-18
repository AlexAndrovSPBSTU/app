package ru.owen.app.model.KippriborMeyrtec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "kippribormeyrteccategory")
@JsonIgnoreProperties({"prices"})
public class KippriborMeyrtecCategory {
    @Id
    @Column(name = "category_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "category")
    private List<CommonPrice> prices;

    public KippriborMeyrtecCategory(Integer id) {
        this.id = String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KippriborMeyrtecCategory owenCategory = (KippriborMeyrtecCategory) o;
        return Objects.equals(id, owenCategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
