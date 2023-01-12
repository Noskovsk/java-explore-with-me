package ru.practicum.exploreit.statistic.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreit.statistic.model.EndpointHit;
import ru.practicum.exploreit.statistic.model.ViewStats;
import ru.practicum.exploreit.statistic.service.StatisticService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/stats")
    public List<ViewStats> getEndpointHit(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                          @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                          @RequestParam(required = false) ArrayList<String> uris,
                                          @RequestParam(required = false) boolean unique) {
        return statisticService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public EndpointHit saveHit(@RequestBody EndpointHit endpointHit) {
        return statisticService.saveHit(endpointHit);
    }
}
