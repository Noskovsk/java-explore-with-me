package ru.practicum.exploreit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreit.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
