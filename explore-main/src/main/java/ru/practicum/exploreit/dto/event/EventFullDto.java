package ru.practicum.exploreit.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.practicum.exploreit.extention.PostgreSQLEnumType;
import ru.practicum.exploreit.model.Category;
import ru.practicum.exploreit.model.EventStatus;
import ru.practicum.exploreit.model.Location;
import ru.practicum.exploreit.model.User;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class EventFullDto implements Comparable<EventFullDto> {
    private Long id;
    private String title;
    private String annotation;
    private String description;
    @Column(name = "event_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime publishedOn;
    private boolean paid;
    private boolean requestModeration;
    private Long participantLimit;
    private User initiator;
    private Location location;
    private Category category;
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private EventStatus state;
    private Long views;
    private Long confirmedRequests;
    private Long rating;

    @Override
    public int compareTo(EventFullDto o) {
        if (this.getEventDate() == null) {
            return 1;
        }
        if (o.getEventDate() == null) {
            return -1;
        } else {
            return (this.getEventDate().isBefore(o.getEventDate()) ? -1 : 1);
        }
    }
}
