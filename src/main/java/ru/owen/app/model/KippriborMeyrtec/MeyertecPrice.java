package ru.owen.app.model.KippriborMeyrtec;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import ru.owen.app.constants.ProjectConstants;

@Entity
@DiscriminatorValue("2")
public class MeyertecPrice extends KippriborMeyrtecPrice {
    @Override
    public void setCategory(KippriborMeyrtecCategory owenCategoryId) {
        super.category = KippriborMeyrtecCategory.builder().id(ProjectConstants.MEYRTEC_PREFIX + owenCategoryId.getId()).build();
    }
}
