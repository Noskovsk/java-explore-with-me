package ru.practicum.exploreit.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.exploreit.model.Category;
import ru.practicum.exploreit.model.User;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class EventShortDto {
    private Long id;
    private String annotation;
    private Category category;
    private Long confirmedRequest;
    @Column(name = "event_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private User initiator;
    private boolean paid;
    private String title;
    private Long views;
}
