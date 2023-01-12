package ru.practicum.exploreit.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreit.statistic.model.EndpointHit;
import ru.practicum.exploreit.statistic.model.ViewStats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT e.app AS app, e.uri AS uri, COUNT(e.ip) AS hits " +
            "FROM endpoint_hit e " +
            "WHERE e.uri in :uris " +
            "AND e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.ip, e.app, e.uri")
    List<ViewStats> getCountEndpointsHits(ArrayList<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT e.app AS app, e.uri AS uri, COUNT(DISTINCT e.ip) AS hits " +
            "FROM endpoint_hit e " +
            "WHERE e.uri in :uris " +
            "AND e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.ip, e.app, e.uri")
    List<ViewStats> getDistinctCountEndpointsHits(ArrayList<String> uris, LocalDateTime start, LocalDateTime end);
}
