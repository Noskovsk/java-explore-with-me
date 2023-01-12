package ru.practicum.exploreit.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreit.dto.ParticipationRequestDto;
import ru.practicum.exploreit.dto.event.EventFullDto;
import ru.practicum.exploreit.dto.event.EventNewDto;
import ru.practicum.exploreit.dto.event.EventShortDto;
import ru.practicum.exploreit.dto.event.EventUpdateDto;
import ru.practicum.exploreit.model.Like;
import ru.practicum.exploreit.service.event.EventService;
import ru.practicum.exploreit.service.like.LikeServiceImpl;
import ru.practicum.exploreit.service.participationrequest.ParticipationRequestServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "users/{userId}")
@AllArgsConstructor
public class PrivateController {
    private final EventService eventService;
    private final ParticipationRequestServiceImpl participationRequestService;
    private final LikeServiceImpl likeService;

    private final boolean like = true;
    private final boolean dislike = false;


    @PostMapping("/events")
    public EventFullDto createEvent(@PathVariable Long userId, @RequestBody @Valid EventNewDto eventNewDto) {
        return eventService.createEvent(userId, eventNewDto);
    }

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(required = false) Integer from,
                                             @RequestParam(required = false) Integer size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getUserEvents(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }

    @PatchMapping("/events")
    public EventFullDto editEventByUser(@PathVariable Long userId, @RequestBody EventUpdateDto eventUpdateDto) {
        return eventService.editEventByUser(userId, eventUpdateDto);
    }

    @PostMapping("/requests")
    public ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return participationRequestService.createRequest(userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return participationRequestService.getEventRequests(userId, eventId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        return participationRequestService.getUserRequests(userId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmUserRequest(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long reqId) {
        return participationRequestService.confirmUserRequest(userId, eventId, reqId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectUserRequest(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long reqId) {
        return participationRequestService.rejectUserRequest(userId, eventId, reqId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelMyRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return participationRequestService.cancelMyRequest(userId, requestId);
    }

    @PutMapping("/like/{eventId}")
    public Like likeEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return likeService.likeOrDislikeEvent(userId, eventId, like);
    }

    @PutMapping("/dislike/{eventId}")
    public Like dislikeEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return likeService.likeOrDislikeEvent(userId, eventId, dislike);
    }

    @DeleteMapping("/like/{eventId}/delete")
    public void deleteLike(@PathVariable Long userId, @PathVariable Long eventId) {
        likeService.deleteLike(userId, eventId);
    }
}
