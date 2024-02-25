package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Mutual.Modification;


@Repository
public interface ModificationRepository extends JpaRepository<Modification, String> {
}
