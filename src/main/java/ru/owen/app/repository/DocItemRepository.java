package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Owen.DocItem;

@Repository
public interface DocItemRepository extends JpaRepository<DocItem, Integer> {
}
