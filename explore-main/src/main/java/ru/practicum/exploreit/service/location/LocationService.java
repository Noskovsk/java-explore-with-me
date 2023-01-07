package ru.practicum.exploreit.service.location;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreit.model.Location;
import ru.practicum.exploreit.repository.LocationRepository;

@Slf4j
@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
}
