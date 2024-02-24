package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.KippriborMeyrtec.KippriborMeyrtecPrice;
import ru.owen.app.model.KippriborMeyrtec.KippriborPrice;

import java.util.List;

@Repository
public interface KippriborMeretecPriceRepository extends JpaRepository<KippriborMeyrtecPrice, String> {
    List<KippriborMeyrtecPrice> findAllByModificationIsNull();

    List<KippriborMeyrtecPrice> findAllByTypeAndNameContainingIgnoreCase(int type, String query);

    default List<KippriborMeyrtecPrice> findAllKippriborProducts(String query) {
        return findAllByTypeAndNameContainingIgnoreCase(1, query);
    }

    default List<KippriborMeyrtecPrice> findAllMeyrtecProducts(String query) {
        return findAllByTypeAndNameContainingIgnoreCase(2, query);
    }
}
