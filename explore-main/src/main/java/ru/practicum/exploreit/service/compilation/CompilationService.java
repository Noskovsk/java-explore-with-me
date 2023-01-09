package ru.practicum.exploreit.service.compilation;

import ru.practicum.exploreit.dto.CompilationDto;
import ru.practicum.exploreit.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation createCompilation(CompilationDto compilationDto);

    void deleteCompilation(Long compId);

    Compilation getCompilationById(Long compId);

    List<Compilation> getCompilationsList(Boolean pinned, Integer from, Integer size);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void unpinCompilation(Long compId);

    void pinCompilation(Long compId);
}
