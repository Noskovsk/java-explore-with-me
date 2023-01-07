package ru.practicum.exploreit.service.participationrequest;

import ru.practicum.exploreit.dto.ParticipationRequestDto;
import ru.practicum.exploreit.model.Event;
import ru.practicum.exploreit.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    ParticipationRequestDto cancelMyRequest(Long userId, Long requestId);

    ParticipationRequest getPartRequest(Long requestId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    List<ParticipationRequest> getConfirmedRequestOfEvent(Event event);

    ParticipationRequestDto confirmUserRequest(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectUserRequest(Long userId, Long eventId, Long reqId);
}
