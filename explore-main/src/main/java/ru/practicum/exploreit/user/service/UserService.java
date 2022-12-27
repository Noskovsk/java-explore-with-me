package ru.practicum.exploreit.user.service;

import ru.practicum.exploreit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> listUsers(ArrayList<Long> ids);

    void deleteUser(Long userId);
}
