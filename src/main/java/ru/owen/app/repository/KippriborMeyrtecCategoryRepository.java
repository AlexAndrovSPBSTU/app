package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.KippriborMeyrtec.KippriborMeyrtecCategory;

import java.util.List;

@Repository
public interface KippriborMeyrtecCategoryRepository extends JpaRepository<KippriborMeyrtecCategory, String> {
    List<KippriborMeyrtecCategory> findByIdStartingWith(String start);
}
