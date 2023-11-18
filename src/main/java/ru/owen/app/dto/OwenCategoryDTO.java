package ru.owen.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class OwenCategoryDTO {
    private String id;

    private String name;

    private String link;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OwenCategoryDTO owenCategory = (OwenCategoryDTO) o;
        return Objects.equals(id, owenCategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
