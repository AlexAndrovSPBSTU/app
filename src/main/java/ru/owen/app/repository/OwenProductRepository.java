package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.CompositeIdClasses.OwenProductId;
import ru.owen.app.model.Owen.OwenProduct;

import java.util.List;

@Repository
public interface OwenProductRepository extends JpaRepository<OwenProduct, OwenProductId> {
    List<OwenProduct> findAllByNameContainingIgnoreCase(String s);
}
