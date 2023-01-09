package ru.practicum.exploreit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreit.model.Event;
import ru.practicum.exploreit.model.ParticipationRequest;
import ru.practicum.exploreit.model.RequestStatus;

import java.util.List;

@Repository
public interface PartRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEvent(Event event);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventAndStatus(Event event, RequestStatus status);

    List<ParticipationRequest> findAllByEventAndStatusNot(Event event, RequestStatus status);

    List<ParticipationRequest> findAllByEventAndStatusNotIn(Event event, List<RequestStatus> statusList);
}
