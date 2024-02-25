package ru.owen.app.model.Mutual;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.owen.app.dto.OwenCategoryDTO;
import ru.owen.app.model.KippriborMeyrtec.KippriborMeyrtecCategory;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoriesResponse {
    private List<OwenCategoryDTO> owen;
    private List<KippriborMeyrtecCategory> kippribor;
    private List<KippriborMeyrtecCategory> meyrtec;
}
