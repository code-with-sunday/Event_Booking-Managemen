package dev.sunday.repository;

import dev.sunday.enums.CATEGORY;
import dev.sunday.model.Event;
import dev.sunday.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findEventByEventId(Long eventId);

    List<Event> findByStartDateTimeOrCategory(Date startDate, CATEGORY category);

    Optional<Event> findByUser(User user);
}
