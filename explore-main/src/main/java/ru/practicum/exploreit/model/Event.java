package ru.practicum.exploreit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;
import ru.practicum.exploreit.extention.PostgreSQLEnumType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
@Table(name = "events")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Length(min = 1, max = 120)
    private String title;
    @NotBlank
    @Length(min = 1, max = 2000)
    private String annotation;
    @NotBlank
    @Length(min = 1, max = 7000)
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
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Column(name = "participant_limit")
    private Long participantLimit;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private EventStatus state;
    @ManyToMany(mappedBy = "events")
    @JsonBackReference
    private Set<Compilation> compilations;
}
