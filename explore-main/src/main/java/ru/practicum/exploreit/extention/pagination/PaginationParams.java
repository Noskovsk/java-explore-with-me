package ru.practicum.exploreit.extention.pagination;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class PaginationParams {
    public static PageRequest createPageRequest(Integer from, Integer size, String orderField) {
        from = validateFrom(from);
        size = validateSize(size);
        return PageRequest.of(from / size, size, Sort.by(orderField).descending());
    }

    public static PageRequest createPageRequest(Integer from, Integer size) {
        from = validateFrom(from);
        size = validateSize(size);
        return PageRequest.of(from / size, size);
    }

    public static Integer validateFrom(Integer from) {
        if (from != null && from < 0) {
            log.error("Неверные параметры пагинации. from {}", from);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверные параметры пагинации.");
        }
        if (from == null) {
            from = 0;
        }
        return from;
    }

    public static Integer validateSize(Integer size) {
        if (size != null && size <= 0) {
            log.error("Неверные параметры пагинации. size {}", size);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверные параметры пагинации.");
        }
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        return size;
    }
}
