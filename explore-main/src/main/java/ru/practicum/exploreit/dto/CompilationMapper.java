package ru.practicum.exploreit.dto;

import ru.practicum.exploreit.model.Compilation;
import ru.practicum.exploreit.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilaion(CompilationDto compilationDto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationDto.isPinned());
        compilation.setTitle(compilationDto.getTitle());
        compilation.setEvents(events);
        return compilation;
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.isPinned());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setEvents(compilation.getEvents().stream().map(event -> event.getId()).collect(Collectors.toList()));
        return compilationDto;
    }
}
