package ticketServiceImplTest;

import dev.sunday.model.Event;
import dev.sunday.model.Ticket;
import dev.sunday.repository.EventRepository;
import dev.sunday.service.serviceImpl.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private EventRepository eventRepository;

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setEventId(1L);
        event.setTickets(new ArrayList<>());

        Ticket ticket1 = new Ticket();
        ticket1.setTicketName("VIP");
        ticket1.setQuantitySold(10);
        ticket1.setQuantityLeft(5);

        Ticket ticket2 = new Ticket();
        ticket2.setTicketName("Regular");
        ticket2.setQuantitySold(20);
        ticket2.setQuantityLeft(10);

        event.getTickets().add(ticket1);
        event.getTickets().add(ticket2);
    }

    @Test
    void shouldReturnTicketInformationWhenEventExists() {
        Long eventId = 1L;
        when(eventRepository.findEventByEventId(eventId)).thenReturn(Optional.of(event));

        Map<String, Map<String, Integer>> result = ticketService.findAllByEventId(eventId);

        assertEquals(2, result.size());
        assertEquals(10, result.get("VIP").get("quantitySold"));
        assertEquals(5, result.get("VIP").get("quantityLeft"));
        assertEquals(20, result.get("Regular").get("quantitySold"));
        assertEquals(10, result.get("Regular").get("quantityLeft"));
    }

    @Test
    void shouldReturnEmptyMapWhenEventDoesNotExist() {
        Long eventId = 2L;
        when(eventRepository.findEventByEventId(eventId)).thenReturn(Optional.empty());

        Map<String, Map<String, Integer>> result = ticketService.findAllByEventId(eventId);

        assertEquals(new HashMap<>(), result);
    }
}