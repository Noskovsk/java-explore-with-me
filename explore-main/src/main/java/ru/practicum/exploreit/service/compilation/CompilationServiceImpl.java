package ru.practicum.exploreit.service.compilation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreit.dto.CompilationDto;
import ru.practicum.exploreit.dto.CompilationMapper;
import ru.practicum.exploreit.extention.pagination.PaginationParams;
import ru.practicum.exploreit.model.Compilation;
import ru.practicum.exploreit.model.Event;
import ru.practicum.exploreit.repository.CompilationRepository;
import ru.practicum.exploreit.service.event.EventServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CompilationServiceImpl {
    private final CompilationRepository compilationRepository;
    private final EventServiceImpl eventService;

    public Compilation createCompilation(CompilationDto compilationDto) {
        log.info("Получен запрос на создание списка событий: {}", compilationDto);
        Set<Event> events = compilationDto.getEvents().stream().map(l -> eventService.getEventById(l)).collect(Collectors.toSet());
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilaion(compilationDto, events));
        return compilation;
    }

    public void deleteCompilation(Long compId) {
        log.info("Получен запрос на удаление подборки compId: {}", compId);
        Compilation compilation = getCompilationById(compId);
        Set<Event> events = compilation.getEvents();
        events.stream().forEach(event -> event.getCompilations().remove(compilation));
        events.stream().forEach(event -> eventService.saveEvent(event));
        compilationRepository.delete(compilation);
    }

    public Compilation getCompilationById(Long compId) {
        Optional<Compilation> compilationOptional = compilationRepository.findById(compId);
        if (compilationOptional.isEmpty()) {
            log.error("Ошибка при поиске подборки событий с compId: {}", compId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при поиске подборки событий!");
        } else {
            return compilationOptional.get();
        }
    }

    public List<Compilation> getCompilationsList(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PaginationParams.createPageRequest(from, size);
        log.info("Получен запрос на получение подборок событий. pinned = {} , from = {}, size = {}", pinned, from, size);
        if (pinned == null) {
            return compilationRepository.findAll(pageRequest).toList();
        }
        return compilationRepository.findAllByPinned(pinned, pageRequest);
    }

    public void deleteEventFromCompilation(Long compId, Long eventId) {
        log.info("Получен запрос на удаление события eventId из подборки compId: {}", compId);
        Compilation compilation = getCompilationById(compId);
        Event event = eventService.getEventById(eventId);
        Set<Event> events = compilation.getEvents();
        if (!events.contains(event)) {
            log.error("Подборка с id {} не содержит события с id {}", compId, eventId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при поиске события в подборке событий!");
        }
        event.getCompilations().remove(compilation);
        events.remove(event);
        eventService.saveEvent(event);
        compilationRepository.save(compilation);
    }

    public void addEventToCompilation(Long compId, Long eventId) {
        log.info("Получен запрос на добавление события eventId в подборку compId: {}", compId);
        Compilation compilation = getCompilationById(compId);
        Event event = eventService.getEventById(eventId);
        Set<Event> events = compilation.getEvents();
        if (events.contains(event)) {
            log.error("Подборка с id {} уже содержит события с id {}", compId, eventId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Подборка уже содержит событие!");
        }
        event.getCompilations().add(compilation);
        events.add(event);
        eventService.saveEvent(event);
        compilationRepository.save(compilation);
    }

    public void unpinCompilation(Long compId) {
        log.info("Получен запрос на открепления подборки compId: {} с главного экрана", compId);
        Compilation compilation = getCompilationById(compId);
        if (!compilation.isPinned()) {
            log.error("Подборка с id {} уже откреплена от главного экрана", compId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Подборка уже откреплена!");
        }
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    public void pinCompilation(Long compId) {
        log.info("Получен запрос на закрепление подборки compId: {} на главноый экран", compId);
        Compilation compilation = getCompilationById(compId);
        if (compilation.isPinned()) {
            log.error("Подборка с id {} уже закреплена", compId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Подборка уже закреплена!");
        }
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}
