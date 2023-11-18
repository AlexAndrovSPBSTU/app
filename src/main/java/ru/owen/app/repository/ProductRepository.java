package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Owen.Product;
import ru.owen.app.model.CompositeIdClasses.ProductId;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductId> {
}
