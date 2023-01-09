package ru.practicum.exploreit.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreit.client.EndpointStatClient;
import ru.practicum.exploreit.dto.event.*;
import ru.practicum.exploreit.exception.BadRequestException;
import ru.practicum.exploreit.exception.ObjectNotFoundException;
import ru.practicum.exploreit.extention.pagination.PaginationParams;
import ru.practicum.exploreit.model.*;
import ru.practicum.exploreit.repository.EventRepository;
import ru.practicum.exploreit.service.category.CategoryService;
import ru.practicum.exploreit.service.location.LocationService;
import ru.practicum.exploreit.service.participationrequest.ParticipationRequestService;
import ru.practicum.exploreit.service.user.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private static final LocalDateTime startSearch = LocalDateTime.of(2020, 01, 01, 00, 00);
    private static final LocalDateTime endSearch = LocalDateTime.of(2099, 01, 01, 00, 00);
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final ParticipationRequestService participationRequestService;
    private final EndpointStatClient endpointStatClient;

    public EventServiceImpl(EventRepository eventRepository,
                            UserService userService,
                            CategoryService categoryService,
                            LocationService locationService,
                            @Lazy ParticipationRequestService participationRequestService, EndpointStatClient endpointHitClient, EndpointStatClient endpointStatClient) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.locationService = locationService;
        this.participationRequestService = participationRequestService;
        this.endpointStatClient = endpointStatClient;
    }

    @Override
    public EventFullDto createEvent(Long userId, @Valid EventNewDto eventNewDto) {
        log.info("Получен запрос от пользователя {} на создание события {}", userId, eventNewDto);
        if (eventNewDto.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            log.error("Мероприятие возможно к регистрации не позднее чем за 2 часа до проведения! userId = {}, eventDate = {}", userId, eventNewDto.getEventDate());
            throw new BadRequestException("Мероприятие возможно к регистрации не позднее чем за 2 часа до проведения!");
        }
        Event event = eventRepository.save(createEvent(eventNewDto, userId));
        return crateFullEvent(event);
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        log.info("Получен запрос на отклонение публикации события {}", eventId);
        Event event = getEventById(eventId);
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            log.error("Невозможно отклонить опобликованную публикацию! eventId = {}, eventState = ", eventId, event.getState());
            throw new BadRequestException("Невозможно отклонить опобликованную публикацию!");
        }
        event.setState(EventStatus.CANCELED);
        return crateFullEvent(eventRepository.save(event));
    }

    @Override
    public EventFullDto approveEvent(Long eventId) {
        log.info("Получен запрос на публикацию события {}", eventId);
        Event event = getEventById(eventId);
        if (event.getState().equals(EventStatus.CANCELED) || event.getState().equals(EventStatus.PUBLISHED)) {
            log.error("Мероприятие возможно к публикации только из статуса PENDING! eventId = {}, eventState = ", eventId, event.getState());
            throw new BadRequestException("Мероприятие возможно к публикации только из статуса PENDING!");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
            log.error("Мероприятие возможно к публикации не позднее чем за 1 часа до проведения! eventDate = {}", event.getEventDate());
            throw new BadRequestException("Мероприятие возможно к публикации не позднее чем за 1 часа до проведения!");
        }
        event.setState(EventStatus.PUBLISHED);
        return crateFullEvent(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        PageRequest pageRequest = PaginationParams.createPageRequest(from, size);
        return eventRepository.findAllByInitiator_Id(userId, pageRequest).stream().map(event -> EventMapper.toShortEvent(event)).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        log.info("Получен запрос от пользователя {} на поиск события с id: {}", userId, eventId);
        Event event = getEventById(eventId);
        if (event.getInitiator().equals(userService.getUser(userId))) {
            return crateFullEvent(event);
        } else {
            log.error("Доступ к события чужих пользователей запрещен! userid={}, eventId={}", userId, eventId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ к события чужих пользователей запрещен!");
        }

    }

    @Override
    public Event getEventById(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            log.error("Ошибка при поиске события с eventId: {}", eventId);
            throw new ObjectNotFoundException("Ошибка при поиске события!");
        } else {
            return eventOptional.get();
        }
    }

    private EventFullDto crateFullEvent(Event event) {
        EventFullDto eventFullDto = EventMapper.toFullEvent(event);
        eventFullDto.setConfirmedRequests(Long.valueOf(participationRequestService.getConfirmedRequestOfEvent(event).size()));
        eventFullDto.setViews(endpointStatClient.getHitsCount(event.getId()));
        return EventMapper.toFullEvent(event);
    }

    private Event createEvent(EventNewDto eventNewDto, Long userId) {
        User initiator = userService.getUser(userId);
        Category category;
        if (eventNewDto.getCategory() != null) {
            category = categoryService.getCategoryById(eventNewDto.getCategory());
        } else {
            category = null;
        }
        Location location;
        if (eventNewDto.getLocation() != null) {
            location = locationService.saveLocation(eventNewDto.getLocation());
        } else {
            location = null;
        }
        return EventMapper.toEventFromNew(eventNewDto, category, initiator, location);
    }

    private Event createEvent(EventUpdateDto eventUpdateDto) {
        Category category;
        if (eventUpdateDto.getCategory() != null) {
            category = categoryService.getCategoryById(eventUpdateDto.getCategory());
        } else {
            category = null;
        }
        return EventMapper.toEventFromUpdate(eventUpdateDto, category);
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        log.info("Получен запрос от пользователя {} на отмену события с id: {}", userId, eventId);
        Event event = getEventById(eventId);
        if (!event.getInitiator().equals(userService.getUser(userId))) {
            log.error("Доступ к события чужих пользователей запрещен! userid={}, eventId={}", userId, eventId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ к события чужих пользователей запрещен!");
        }
        if (!event.getState().equals(EventStatus.PENDING)) {
            log.error("Невозможно отклонить опобликованную публикацию!");
            throw new BadRequestException("Невозможно отклонить опобликованную публикацию!");
        }
        event.setState(EventStatus.CANCELED);
        return EventMapper.toFullEvent(eventRepository.save(event));
    }

    @Override
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users,
                                               List<EventStatus> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Integer from,
                                               Integer size) {
        log.info("Получен запрос на поиск по параметрам " +
                        "users = {}, states = {}, categories = {}, rangeStart = {}, rangeEnd = {}",
                users, states, categories, rangeStart, rangeEnd);
        PageRequest pageRequest = PaginationParams.createPageRequest(from, size);
        List<User> userList = userService.listUsers(users, null, null);
        List<Category> categoryList = categoryService.getCategoryListById(categories);

        return eventRepository.findEventByAdminFilter(
                userList,
                states == null ? List.of(EventStatus.values()) : states,
                categoryList,
                rangeStart == null ? endSearch : rangeStart,
                rangeEnd == null ? startSearch : rangeEnd,
                pageRequest).stream().map(e -> EventMapper.toFullEvent(e)).collect(Collectors.toList());
    }

    @Override
    public EventFullDto editEventByAdmin(Long eventId, EventNewDto eventNewDto) {
        log.info("Получен запрос на редактирование собтия id {} новые данные: {}", eventId, eventNewDto);
        Event oldEvent = getEventById(eventId);
        Event editedEvent = createEvent(eventNewDto, oldEvent.getInitiator().getId());
        oldEvent = eventRepository.save(EventMapper.editEvent(editedEvent, oldEvent));
        return crateFullEvent(oldEvent);
    }

    @Override
    public EventFullDto editEventByUser(Long userId, EventUpdateDto eventUpdateDto) {
        log.info("Получен запрос от пользователя userId={} на редактирование собтия id {} новые данные: {}", userId, eventUpdateDto.getEventId(), eventUpdateDto);
        Event oldEvent = getEventById(eventUpdateDto.getEventId());
        if (!oldEvent.getInitiator().equals(userService.getUser(userId))) {
            log.error("Доступ к события чужих пользователей запрещен! userid={}, eventId={}", userId, eventUpdateDto.getEventId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ к события чужих пользователей запрещен!");
        }
        if (oldEvent.getState().equals(EventStatus.PUBLISHED)) {
            log.error("Редактировать возможно только события кторые не подтверждены!");
            throw new BadRequestException("Редактировать возможно только события кторые не подтверждены!");
        }
        LocalDateTime eventDate = eventUpdateDto.getEventDate() == null ? oldEvent.getEventDate() : eventUpdateDto.getEventDate();
        if (eventDate.isBefore(LocalDateTime.now().minusHours(2))) {
            log.error("Мероприятие возможно редактировать не позднее чем за 2 часа до проведения!");
            throw new BadRequestException("Мероприятие возможно редактировать не позднее чем за 2 часа до проведения!");
        }
        if (oldEvent.getState().equals(EventStatus.CANCELED)) {
            oldEvent.setState(EventStatus.PENDING);
        }
        Event editedEvent = createEvent(eventUpdateDto);
        oldEvent = eventRepository.save(EventMapper.editEvent(editedEvent, oldEvent));
        return crateFullEvent(oldEvent);
    }

    @Override
    public EventFullDto getEventInfo(Long eventId, String uri, String ip) throws InterruptedException {
        Event event = getEventById(eventId);
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            log.error("Доступ к мероприятию возможен только только из статуса PUBLISHED! eventId = {}, eventState = {}", eventId, event.getState());
            throw new BadRequestException("Доступ к мероприятию возможен только только из статуса PUBLISHED!");
        }
        endpointStatClient.sendHit(uri, ip);
        return crateFullEvent(event);
    }

    @Override
    public List<EventFullDto> getEventsByFilter(String text,
                                                List<Long> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Boolean onlyAvailable,
                                                String sort,
                                                Integer from,
                                                Integer size,
                                                String uri,
                                                String ip) {
        log.info("Получен запрос на поиск по параметрам " +
                        "text = {}, categories = {}, paid = {}, rangeStart = {}, rangeEnd = {}, onlyAvailable = {}, sort = {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);
        List<Category> categoryList = categories == null ? null : categoryService.getCategoryListById(categories);
        if (sort != null) {
            SortParam sortOrder = stringToSortParam(sort);
        }
        PageRequest pageRequest = PaginationParams.createPageRequest(from, size);
        List<EventFullDto> eventFullDtoList = eventRepository.findEventByFilter(text,
                categoryList,
                paid,
                rangeStart == null ? startSearch : rangeStart,
                rangeEnd == null ? endSearch : rangeEnd,
                pageRequest).stream().map(e -> EventMapper.toFullEvent(e)).collect(Collectors.toList());
        if (onlyAvailable != null && onlyAvailable) {
            eventFullDtoList.removeIf(e -> (e.getParticipantLimit() > 0 && e.getConfirmedRequests() >= e.getParticipantLimit()));
        }
        endpointStatClient.sendHit(uri, ip);
        return eventFullDtoList;
    }

    private SortParam stringToSortParam(String sort) {
        try {
            return SortParam.valueOf(sort);
        } catch (IllegalArgumentException e) {
            log.error("Неподдерживаемый тип сортировки! sort = {}", sort);
            throw new BadRequestException("Неподдерживаемый тип сортировки " + sort + " .");
        }
    }
}
