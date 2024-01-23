package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.KippriborMeyrtec.CommonPrice;

import java.util.List;

@Repository
public interface KippriborMeretecPriceRepository extends JpaRepository<CommonPrice, String> {
    List<CommonPrice> findAllByModificationIsNull();
}
