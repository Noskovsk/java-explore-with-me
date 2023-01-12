package ru.practicum.exploreit.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.practicum.exploreit.extention.PostgreSQLEnumType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "participation_requests")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private RequestStatus status;
}
