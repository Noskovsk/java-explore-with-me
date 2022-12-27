package ru.practicum.exploreit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreit.user.dao.UserRepository;
import ru.practicum.exploreit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        log.info("Получен запрос на создание пользователя: name = {}, email = {}", user.getName(), user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public List<User> listUsers(ArrayList<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            log.info("Получен запрос на получение списка пользователей");
            return userRepository.findAll();
        } else {
            log.info("Получен запрос на поиск пользователей с id: {}", ids);
            List<User> userList = userRepository.getUsersByIdIn(ids);
            return userList;
//            if (userList.isEmpty()) {
//                log.error("Ошибка при поиске пользователей с userId: {}", ids);
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при поиске пользователя!");
//            } else {
//                return userList;
//            }
        }
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Получен запрос на удаление пользователя с id {}", userId);
        userRepository.deleteById(userId);
    }
}
