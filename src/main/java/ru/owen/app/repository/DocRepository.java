package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Owen.Doc;

@Repository
public interface DocRepository extends JpaRepository<Doc, Integer> {
}
