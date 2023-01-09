package ru.practicum.exploreit.statistic.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity(name = "endpoint_hit")
@Getter
@Setter
@ToString
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(name = "app")
    private String app;
    @NotBlank
    @Length(min = 1, max = 2000)
    @Column(name = "uri")
    private String uri;
    @NotBlank
    @Length(min = 1, max = 255)
    @Column(name = "ip")
    private String ip;
    @Column(name = "timestamp", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
