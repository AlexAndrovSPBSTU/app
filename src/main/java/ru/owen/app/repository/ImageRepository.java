package ru.owen.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.owen.app.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
