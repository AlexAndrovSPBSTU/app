package ru.owen.app.model.KippriborMeyrtec;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ru.owen.app.constants.ProjectConstants;

@Entity
@DiscriminatorValue("1")
public class KippriborPrice extends CommonPrice {
    @Override
    public void setCategory(KippriborMeyrtecCategory owenCategoryId) {
        super.category = KippriborMeyrtecCategory.builder().id(ProjectConstants.KIPPRIBOR_PREFIX + owenCategoryId.getId()).build();
    }
}
