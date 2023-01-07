package ru.practicum.exploreit.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreit.dto.user.UserDto;
import ru.practicum.exploreit.dto.user.UserMapper;
import ru.practicum.exploreit.extention.pagination.PaginationParams;
import ru.practicum.exploreit.model.User;
import ru.practicum.exploreit.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.info("Получен запрос на создание пользователя: name = {}, email = {}", user.getName(), user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public List<User> listUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest pageRequest = PaginationParams.createPageRequest(from, size);
        if (ids == null || ids.isEmpty()) {
            log.info("Получен запрос на получение списка пользователей");
            return userRepository.findAll(pageRequest).stream().collect(Collectors.toList());
        } else {
            log.info("Получен запрос на поиск пользователей с id: {}", ids);
            List<User> userList = userRepository.getUsersByIdIn(ids, pageRequest);
            return userList;
        }
    }

    @Override
    public User getUser(Long userId) {
        log.info("Получен запрос на поиск пользователя с id: {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при поиске пользователя с userId: {}", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при поиске пользователя!");
        } else {
            return userOptional.get();
        }
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Получен запрос на удаление пользователя с id {}", userId);
        userRepository.deleteById(userId);
    }


}
