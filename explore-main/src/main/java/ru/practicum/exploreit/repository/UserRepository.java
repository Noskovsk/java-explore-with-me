package ru.practicum.exploreit.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreit.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getUsersByIdIn(List<Long> ids, PageRequest pageRequest);
}
