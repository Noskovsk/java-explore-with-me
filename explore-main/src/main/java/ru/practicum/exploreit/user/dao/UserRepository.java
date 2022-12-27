package ru.practicum.exploreit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getUsersByIdIn(ArrayList<Long> ids);
}
