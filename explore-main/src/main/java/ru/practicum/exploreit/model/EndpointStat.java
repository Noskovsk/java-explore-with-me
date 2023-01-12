package ru.practicum.exploreit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EndpointStat {
    private String app;
    private String uri;
    private Long hits;
}
