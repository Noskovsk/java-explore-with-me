package ru.practicum.exploreit.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exploreit.dto.event.EventFullDto;
import ru.practicum.exploreit.model.Category;
import ru.practicum.exploreit.model.Compilation;
import ru.practicum.exploreit.service.category.CategoryService;
import ru.practicum.exploreit.service.compilation.CompilationServiceImpl;
import ru.practicum.exploreit.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class PublicController {
    private final CategoryService categoryService;
    private final CompilationServiceImpl compilationService;
    private final EventService eventService;

    @GetMapping("categories")
    public List<Category> listCategories(@RequestParam(required = false) Integer from,
                                         @RequestParam(required = false) Integer size) {
        return categoryService.listCategories(from, size);
    }

    @GetMapping("categories/{catId}")
    public Category getCategoryById(@PathVariable long catId) {
        return categoryService.getCategoryById(catId);
    }

    @GetMapping("/compilations")
    public List<Compilation> getCompilationsList(@RequestParam(required = false) Boolean pinned,
                                                 @RequestParam(required = false) Integer from,
                                                 @RequestParam(required = false) Integer size) {
        return compilationService.getCompilationsList(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public Compilation getCompilationById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventInfo(@PathVariable Long eventId, HttpServletRequest request) throws InterruptedException {
        return eventService.getEventInfo(eventId, request.getRequestURI(), request.getRemoteAddr());
    }

    @GetMapping("/events")
    public List<EventFullDto> getEventsByFilter(HttpServletRequest request,
                                                @RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(required = false) Boolean onlyAvailable,
                                                @RequestParam(required = false) String sort,
                                                @RequestParam(required = false) Integer from,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsByFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRequestURI(), request.getRemoteAddr());

    }

}
