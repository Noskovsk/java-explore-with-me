package ru.practicum.exploreit.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreit.model.Category;
import ru.practicum.exploreit.model.Event;
import ru.practicum.exploreit.model.EventStatus;
import ru.practicum.exploreit.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator_Id(Long initiatorId, PageRequest pageRequest);

    @Query("SELECT e FROM Event e WHERE (:users is null or e.initiator in :users) " +
            "AND (e.state in :states) " +
            "AND (:categories is null or e.category in :categories) " +
            "AND (e.eventDate >= :start) " +
            "AND (e.eventDate <= :end)")
    List<Event> findEventByAdminFilter(@Param("users") List<User> users,
                                       @Param("states") List<EventStatus> states,
                                       @Param("categories") List<Category> categories,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       PageRequest pageRequest);

    @Query("SELECT e FROM Event e WHERE (:text is null or e.annotation LIKE %:text%) " +
            "AND (:categories is null or e.category in :categories) " +
            "AND (:paid is null or e.paid = :paid) " +
            "AND (e.eventDate >= :rangeStart) " +
            "AND (e.eventDate <= :rangeEnd)")
    List<Event> findEventByFilter(@Param("text") String text,
                                  @Param("categories") List<Category> categories,
                                  @Param("paid") Boolean paid,
                                  @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd,
                                  PageRequest pageRequest);
}
