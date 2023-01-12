package ru.practicum.exploreit.service.user;

import ru.practicum.exploreit.dto.user.UserDto;
import ru.practicum.exploreit.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);

    List<User> listUsers(List<Long> ids, Integer from, Integer size);

    User getUser(Long id);

    void deleteUser(Long userId);
}
