package ru.practicum.exploreit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreit.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIdIn(List<Long> ids);
}
