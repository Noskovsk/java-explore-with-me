package ru.practicum.exploreit.dto.event;

import org.modelmapper.ModelMapper;
import ru.practicum.exploreit.model.*;

import java.time.LocalDateTime;

public class EventMapper {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static Event toEventFromNew(EventNewDto eventNewDto, Category category, User initiator, Location location) {
        Event event = MODEL_MAPPER.map(eventNewDto, Event.class);
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventStatus.PENDING);
        event.setLocation(location);
        return event;
    }

    public static Event toEventFromUpdate(EventUpdateDto eventUpdateDto, Category category) {
        Event event = MODEL_MAPPER.map(eventUpdateDto, Event.class);
        event.setCategory(category);
        return event;
    }

    public static EventFullDto toFullEvent(Event event) {
        return MODEL_MAPPER.map(event, EventFullDto.class);
    }

    public static EventShortDto toShortEvent(Event event) {
        return MODEL_MAPPER.map(event, EventShortDto.class);
    }

    public static Event editEvent(Event newEventData, Event eventToBeEdited) {
        MODEL_MAPPER.getConfiguration().setSkipNullEnabled(true);
        MODEL_MAPPER.map(newEventData, eventToBeEdited);
        return eventToBeEdited;
    }
}
