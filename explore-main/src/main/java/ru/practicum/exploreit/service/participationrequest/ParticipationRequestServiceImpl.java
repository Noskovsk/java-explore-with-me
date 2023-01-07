package ru.practicum.exploreit.service.participationrequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreit.dto.ParticipationMapper;
import ru.practicum.exploreit.dto.ParticipationRequestDto;
import ru.practicum.exploreit.model.*;
import ru.practicum.exploreit.repository.PartRequestRepository;
import ru.practicum.exploreit.service.event.EventServiceImpl;
import ru.practicum.exploreit.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final PartRequestRepository partRequestRepository;
    private final EventServiceImpl eventService;
    private final UserService userService;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        log.info("Получен запрос на создание заявки на участие в событии id = {} от пользователя с id = {}", eventId, userId);
        User user = userService.getUser(userId);
        Event event = eventService.getEventById(eventId);
        if (user.equals(event.getInitiator())) {
            log.error("Инициатор события не может добавить запрос на участие в своём событии! userid={}, eventId={}", userId, eventId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Инициатор события не может добавить запрос на участие в своём событии!");
        }
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            log.error("Нельзя участвовать в неопубликованном событии! userid={}, eventId={}", userId, eventId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нельзя участвовать в неопубликованном событии!");
        }
        int countActiveRequests = partRequestRepository.findAllByEventAndStatusNotIn(event, List.of(RequestStatus.CANCELED, RequestStatus.REJECTED)).size();
        if (event.getParticipantLimit() > 0 && countActiveRequests >= event.getParticipantLimit()) {
            log.error("Достигнут лимит по количеству участников в событии eventId={}! userid={}", eventId, userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Достигнут лимит по количеству участников в событии!");
        }
        ParticipationRequest pr = new ParticipationRequest();
        pr.setCreated(LocalDateTime.now());
        pr.setEvent(event);
        pr.setRequester(user);
        pr.setStatus(event.isRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED);
        return ParticipationMapper.toParticipationRequestDto(partRequestRepository.save(pr));
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        log.info("Получен запрос на поиск заявок на участие в событии id = {} от пользователя с id = {}", eventId, userId);
        User user = userService.getUser(userId);
        Event event = eventService.getEventById(eventId);
        if (!user.equals(event.getInitiator())) {
            log.error("Запрашивать информацию может только инициатор события! userid={}, eventId={}", userId, eventId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Запрашивать информацию может только инициатор события!");
        }
        return partRequestRepository.findAllByEvent(event).stream().map(part -> ParticipationMapper.toParticipationRequestDto(part)).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelMyRequest(Long userId, Long requestId) {
        log.info("Получен запрос на отмены заявки на участие id = {} от пользователя с id = {}", requestId, userId);
        ParticipationRequest participationRequest = getPartRequest(requestId);
        User user = userService.getUser(userId);
        if (!participationRequest.getRequester().equals(user)) {
            log.error("Отменить запрос может только инициатор запроса! userid={}, requestId={}", userId, requestId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Отменить запрос может только инициатор запроса!");
        }
        if (!participationRequest.getStatus().equals(RequestStatus.PENDING)) {
            log.error("Отклонять заявку можно только из статуса PENDING! userid={}, requestId={}", userId, requestId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Отклонять заявку можно только из статуса PENDING!");
        }
        participationRequest.setStatus(RequestStatus.CANCELED);
        return ParticipationMapper.toParticipationRequestDto(partRequestRepository.save(participationRequest));
    }

    @Override
    public ParticipationRequest getPartRequest(Long requestId) {
        Optional<ParticipationRequest> requestOptional = partRequestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            log.error("Ошибка при поиске запроса на участие requestId: {}", requestId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при поиске события!");
        } else {
            return requestOptional.get();
        }
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        return partRequestRepository.findAllByRequesterId(userId).stream().map(p -> ParticipationMapper.toParticipationRequestDto(p)).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequest> getConfirmedRequestOfEvent(Event event) {
        return partRequestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED);
    }

    @Override
    public ParticipationRequestDto confirmUserRequest(Long userId, Long eventId, Long reqId) {
        log.info("Получен запрос на подтверждение заявки на участие id = {} в событии eventId = {}, от пользователя с id = {}", reqId, eventId, userId);

        User user = userService.getUser(userId);
        Event event = eventService.getEventById(eventId);

        if (!user.equals(event.getInitiator())) {
            log.error("Акцептовать заявки может только инициатор события! userid={}, eventId={}", userId, eventId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Акцептовать заявки может только инициатор события!");
        }
        ParticipationRequest participationRequest = getPartRequest(reqId);
        if (participationRequest.getEvent().getId() != eventId) {
            log.info("Событие не относится к запросу. id запроса = {}, id события  = {}, id пользователя = {}", reqId, eventId, userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Событие не относится к запросу.");
        }
        if (!participationRequest.getStatus().equals(RequestStatus.PENDING)) {
            log.error("Подтвердить заявку можно только из статуса PENDING! id запроса = {}, id события  = {}, id пользователя = {}", reqId, eventId, userId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Подтвердить заявку можно только из статуса PENDING!");
        }
        if (event.getParticipantLimit() > 0) {
            int countApproved = partRequestRepository.findAllByEventAndStatus(event, RequestStatus.CONFIRMED).size();
            if (countApproved < event.getParticipantLimit()) {
                participationRequest.setStatus(RequestStatus.CONFIRMED);
                participationRequest = partRequestRepository.save(participationRequest);
                countApproved++;
                if (countApproved == event.getParticipantLimit()) {
                    List<ParticipationRequest> participationRequestList = partRequestRepository.findAllByEventAndStatusNot(event, RequestStatus.CONFIRMED);
                    participationRequestList.stream().forEach(pR -> pR.setStatus(RequestStatus.REJECTED));
                    participationRequestList.stream().forEach(pR -> partRequestRepository.save(pR));
                }
                return ParticipationMapper.toParticipationRequestDto(participationRequest);
            } else {
                log.error("Достигнут лимит по количеству участников в событии eventId={}! userid={}", eventId, userId);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "остигнут лимит по количеству участников в событии!");
            }
        } else {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
            return ParticipationMapper.toParticipationRequestDto(partRequestRepository.save(participationRequest));
        }
    }

    @Override
    public ParticipationRequestDto rejectUserRequest(Long userId, Long eventId, Long reqId) {
        log.info("Получен запрос на отклонение заявки на участие id = {} в событии eventId = {}, от пользователя с id = {}", reqId, eventId, userId);
        ParticipationRequest participationRequest = getPartRequest(reqId);
        if (!participationRequest.getStatus().equals(RequestStatus.PENDING)) {
            log.error("Отклонять заявку можно только из статуса PENDING! userid={}, requestId={}", userId, reqId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Отклонять заявку можно только из статуса PENDING!");
        }
        User user = userService.getUser(userId);
        Event event = eventService.getEventById(eventId);
        if (!user.equals(event.getInitiator())) {
            log.error("Отклонять заявки может только инициатор события! userid={}, eventId={}", userId, eventId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Отклонять заявки может только инициатор события!");
        }
        participationRequest.setStatus(RequestStatus.REJECTED);
        return ParticipationMapper.toParticipationRequestDto(partRequestRepository.save(participationRequest));
    }
}
