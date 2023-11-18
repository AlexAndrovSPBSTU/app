package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Owen.OwenCategory;

import java.util.List;

@Repository
public interface OwenCategoryRepository extends JpaRepository<OwenCategory, String> {
    List<OwenCategory> findALlByParentIsNull();

    List<OwenCategory> findAllByParentId(String parent);
}
