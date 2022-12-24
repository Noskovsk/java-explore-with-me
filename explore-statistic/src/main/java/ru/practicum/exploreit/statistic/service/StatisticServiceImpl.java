package ru.practicum.exploreit.statistic.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreit.statistic.model.EndpointHit;
import ru.practicum.exploreit.statistic.model.ViewStats;
import ru.practicum.exploreit.statistic.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, boolean unique) {
        log.info("Получен запрос на поиск статистики. Параметры: start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return statisticRepository.getCountEndpointsHits(uris, start, end);
    }

    @Override
    public EndpointHit saveHit(EndpointHit endpointHit) {
        log.info("Получен запрос на сохранение endpointHit={}", endpointHit);
        return statisticRepository.save(endpointHit);
    }
}
