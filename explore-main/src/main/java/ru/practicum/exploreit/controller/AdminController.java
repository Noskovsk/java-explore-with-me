package ru.practicum.exploreit.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreit.dto.CompilationDto;
import ru.practicum.exploreit.dto.event.EventFullDto;
import ru.practicum.exploreit.dto.event.EventNewDto;
import ru.practicum.exploreit.dto.user.UserDto;
import ru.practicum.exploreit.model.Category;
import ru.practicum.exploreit.model.Compilation;
import ru.practicum.exploreit.model.EventStatus;
import ru.practicum.exploreit.model.User;
import ru.practicum.exploreit.service.category.CategoryService;
import ru.practicum.exploreit.service.compilation.CompilationService;
import ru.practicum.exploreit.service.event.EventService;
import ru.practicum.exploreit.service.user.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "admin")
@AllArgsConstructor
public class AdminController {
    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @PostMapping("/categories")
    public Category createCategory(@RequestBody @Valid Category category) {
        return categoryService.createCategory(category);
    }

    @PatchMapping("/categories")
    public Category updateCategory(@RequestBody @Valid Category category) {
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    @GetMapping("/users")
    public List<User> listUsers(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(required = false) Integer from,
                                @RequestParam(required = false) Integer size) {
        return userService.listUsers(ids, from, size);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PostMapping("/compilations")
    public Compilation createCompilation(@RequestBody @Valid CompilationDto compilationDto) {
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto approveEvent(@PathVariable Long eventId) {
        return eventService.approveEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        return eventService.rejectEvent(eventId);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto editEvent(@PathVariable Long eventId, @RequestBody EventNewDto eventNewDto) {
        return eventService.editEventByAdmin(eventId, eventNewDto);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        compilationService.pinCompilation(compId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                               @RequestParam(required = false) List<EventStatus> states,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(required = false) Integer from,
                                               @RequestParam(required = false) Integer size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
