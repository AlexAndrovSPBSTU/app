package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.KippriborMeyrtec.Arrival;

@Repository
public interface ArrivalRepository extends JpaRepository<Arrival, Integer> {
}
