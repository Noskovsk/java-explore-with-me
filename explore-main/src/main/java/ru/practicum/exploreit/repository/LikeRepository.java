package ru.practicum.exploreit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreit.model.Event;
import ru.practicum.exploreit.model.Like;
import ru.practicum.exploreit.model.User;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findLikeByEventAndUser(Event event, User user);
}
