package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.OwenImage;

@Repository
public interface ImageRepository extends JpaRepository<OwenImage, Integer> {
}
