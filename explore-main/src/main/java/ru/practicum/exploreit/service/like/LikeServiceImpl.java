package ru.practicum.exploreit.service.like;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreit.exception.BadRequestException;
import ru.practicum.exploreit.exception.ObjectNotFoundException;
import ru.practicum.exploreit.model.Event;
import ru.practicum.exploreit.model.EventStatus;
import ru.practicum.exploreit.model.Like;
import ru.practicum.exploreit.model.User;
import ru.practicum.exploreit.repository.LikeRepository;
import ru.practicum.exploreit.service.event.EventService;
import ru.practicum.exploreit.service.user.UserService;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class LikeServiceImpl {
    private final EventService eventService;
    private final UserService userService;
    private final LikeRepository likeRepository;

    public Like likeOrDislikeEvent(Long userId, Long eventId, boolean isLike) {
        log.info("Получен запрос на изменение рейтинга события eventId = {}, от пользователя с id = {}, isLike={}", eventId, userId, isLike);
        User user = userService.getUser(userId);
        Event event = eventService.getEventById(eventId);
        if (user.equals(event.getInitiator())) {
            log.error("Инициатор события не может ставить лайк своему событию! userid={}, eventId={}", userId, eventId);
            throw new BadRequestException("Инициатор события не может ставить лайк своему событию!");
        }
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            log.error("Нельзя отдавать лайк в неопубликованному событию! userid={}, eventId={}", userId, eventId);
            throw new BadRequestException("Нельзя отдавать лайк в неопубликованному событию!");
        }
        Like like = Like.builder()
                .event(event)
                .user(user)
                .isLike(isLike)
                .build();

        return likeRepository.save(like);
    }

    public void deleteLike(Long userId, Long eventId) {
        log.info("Получен запрос на удаление рейтинга события eventId = {}, от пользователя с id = {}", eventId, userId);
        Like like = getLikeByUserIdAndEventId(userId, eventId);
        likeRepository.delete(like);
    }

    private Like getLikeByUserIdAndEventId(Long userId, Long eventId) {
        User user = userService.getUser(userId);
        Event event = eventService.getEventById(eventId);
        Optional<Like> optionalLike = likeRepository.findLikeByEventAndUser(event, user);
        if (optionalLike.isEmpty()) {
            log.error("Ошибка при поиске лайка события eventId = {}, от пользователя с id = {}", eventId, userId);
            throw new ObjectNotFoundException("Ошибка при поиске лайка события!");
        } else {
            return optionalLike.get();
        }
    }
}