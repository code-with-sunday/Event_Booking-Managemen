package dev.sunday.service.serviceImpl;

import dev.sunday.model.Event;
import dev.sunday.model.Ticket;
import dev.sunday.repository.EventRepository;
import dev.sunday.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final EventRepository eventRepository;



    @Override
    public Map<String, Map<String, Integer>> findAllByEventId(Long eventId) {
        Optional<Event> event =  eventRepository.findEventByEventId(eventId);

        if (event.isPresent()) {
            Event targetEvent = event.get();

            List<Ticket> eventTickets = targetEvent.getTickets();

            Map<String, Map<String, Integer>> ticketInformation = new HashMap<>();

            for (Ticket ticket : eventTickets) {
                String ticketCategory = ticket.getTicketName();
                int quantitySold = ticket.getQuantitySold();
                int quantityLeft = ticket.getQuantityLeft();

                Map<String, Integer> categoryInfo = new HashMap<>();
                categoryInfo.put("quantitySold", quantitySold);
                categoryInfo.put("quantityLeft", quantityLeft);

                ticketInformation.put(ticketCategory, categoryInfo);
            }

            return ticketInformation;
        } else {
            return new HashMap<>();
        }
    }
}
