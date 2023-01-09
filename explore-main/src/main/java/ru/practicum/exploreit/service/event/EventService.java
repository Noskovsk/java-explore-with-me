package ru.practicum.exploreit.service.event;

import ru.practicum.exploreit.dto.event.EventFullDto;
import ru.practicum.exploreit.dto.event.EventNewDto;
import ru.practicum.exploreit.dto.event.EventShortDto;
import ru.practicum.exploreit.dto.event.EventUpdateDto;
import ru.practicum.exploreit.model.Event;
import ru.practicum.exploreit.model.EventStatus;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, @Valid EventNewDto eventNewDto);

    EventFullDto rejectEvent(Long eventId);

    EventFullDto approveEvent(Long eventId);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto getUserEventById(Long userId, Long eventId);

    Event getEventById(Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    Event saveEvent(Event event);

    List<EventFullDto> getEventsByAdmin(List<Long> users,
                                        List<EventStatus> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Integer from,
                                        Integer size);

    EventFullDto editEventByAdmin(Long eventId, EventNewDto eventNewDto);

    EventFullDto editEventByUser(Long userId, EventUpdateDto eventUpdateDto);

    EventFullDto getEventInfo(Long eventId, String uri, String ip) throws InterruptedException;

    List<EventFullDto> getEventsByFilter(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         Integer from,
                                         Integer size,
                                         String uri,
                                         String ip);
}
