package ru.practicum.exploreit.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@Getter
@Setter
public class CompilationDto {
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    private boolean pinned;
    private List<Long> events;
}
