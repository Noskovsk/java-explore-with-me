package ru.practicum.exploreit.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exploreit.model.EndpointHit;
import ru.practicum.exploreit.model.EndpointStat;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EndpointStatClient {
    private static final String uriHead = "/events/";
    private static final LocalDateTime startSearchStat = LocalDateTime.of(2020, 01, 01, 00, 00);
    private static final LocalDateTime endSearchStat = LocalDateTime.of(2099, 01, 01, 00, 00);
    private final String APP = "app";
    @Value("${exploreit-stat-server.url}")
    private String serverUrl;

    public void sendHit(HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit(APP, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        HttpEntity entity = new HttpEntity(endpointHit, createHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, entity, String.class);
        if (responseEntity.getStatusCodeValue() != 200) {
            log.error("Ошибка при попытке записи в статистику!");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при попытке записи в статистику!");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    public Long getHitsCount(Long eventId) {
        HttpEntity entity = new HttpEntity(createHeaders());
        RestTemplate restTemplate = new RestTemplate();

        String uri = uriHead + eventId;

        Map<String, Object> parameters = Map.of(
                "start", startSearchStat.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "end", endSearchStat.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "uris", uri
        );

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(serverUrl + "/stats?start={start}&end={end}&uris={uris}", HttpMethod.GET, entity, String.class, parameters);
            Type collectionType = new TypeToken<List<EndpointStat>>() {
            }.getType();
            List<EndpointStat> endpointStatList = new Gson().fromJson(responseEntity.getBody(), collectionType);
            if (endpointStatList.size() == 1) {
                return endpointStatList.get(0).getHits();
            } else {
                return 0L;
            }
        } catch (HttpStatusCodeException e) {
            log.error("Ошибка при попытке поиска статистики!" + e.getStatusCode() + " " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при попытке поиска статистики!");
        }

    }


}
