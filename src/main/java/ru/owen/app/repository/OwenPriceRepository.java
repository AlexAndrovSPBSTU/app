package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Owen.OwenPrice;
import ru.owen.app.model.CompositeIdClasses.PriceId;

import java.util.List;

@Repository
public interface OwenPriceRepository extends JpaRepository<OwenPrice, PriceId> {
    List<OwenPrice> findALlByModificationIsNull();
}
