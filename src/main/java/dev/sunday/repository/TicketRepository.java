package dev.sunday.repository;

import dev.sunday.model.Event;
import dev.sunday.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findById(Long ticketId);

    Ticket findByTicketId (Long ticketId);

    List<Ticket> findAllByEvent(Event event);
}
