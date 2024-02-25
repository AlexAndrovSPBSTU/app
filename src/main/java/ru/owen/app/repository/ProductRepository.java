package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Owen.OwenProduct;
import ru.owen.app.model.CompositeIdClasses.OwenProductId;

@Repository
public interface ProductRepository extends JpaRepository<OwenProduct, OwenProductId> {
}
