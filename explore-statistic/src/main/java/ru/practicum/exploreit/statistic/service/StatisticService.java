package ru.practicum.exploreit.statistic.service;

import ru.practicum.exploreit.statistic.model.EndpointHit;
import ru.practicum.exploreit.statistic.model.ViewStats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface StatisticService {
    EndpointHit saveHit(EndpointHit endpointHit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, boolean unique);
}
