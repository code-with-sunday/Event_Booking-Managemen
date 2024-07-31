package eventServiceImplTest;

import dev.sunday.DTO.request.EventDTO;
import dev.sunday.DTO.request.SearchResultDTO;
import dev.sunday.enums.CATEGORY;
import dev.sunday.exception.ResourceNotFoundException;
import dev.sunday.model.Event;
import dev.sunday.model.Ticket;
import dev.sunday.model.User;
import dev.sunday.repository.EventRepository;
import dev.sunday.repository.TicketRepository;
import dev.sunday.repository.UserRepository;
import dev.sunday.service.serviceImpl.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    private EventServiceImpl eventService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(ticketRepository, eventRepository, userRepository);
    }

    @Test
    void shouldSaveEventSuccessfully() {

        EventDTO eventDTO = EventDTO.builder()
                .eventDescription("Sample Event")
                .category(CATEGORY.CONCERT)
                .startDateTime(new Date())
                .endDateTime(new Date())
                .ticketsInfo(new ArrayList<>())
                .build();

        User user = User.builder().email("sundaypetersp12@gmail.com").build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(user.getEmail());

        Event savedEvent = Event.builder()
                .eventId(1L)
                .eventDescription("Sample Event")
                .category(CATEGORY.CONCERT)
                .startDateTime(eventDTO.getStartDateTime())
                .endDateTime(eventDTO.getEndDateTime())
                .user(user)
                .tickets(new ArrayList<>())
                .build();

        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(ticketRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        ResponseEntity<EventDTO> response = eventService.saveEvent(eventDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedEvent.getEventDescription(), response.getBody().getEventDescription());
    }

    @Test
    void shouldDeleteEventSuccessfully() {

        Long eventId = 1L;
        String userEmail = "creator@example.com";
        User user = User.builder().email(userEmail).build();
        Event event = Event.builder()
                .eventId(eventId)
                .user(user)
                .ticketSold(0)
                .tickets(new ArrayList<>())
                .endDateTime(new Date(System.currentTimeMillis() + 86400000))
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        ResponseEntity<String> response = eventService.deleteEvent(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Event deleted successfully", response.getBody());
        verify(ticketRepository).deleteAll(event.getTickets());
        verify(eventRepository).delete(event);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenEventNotFound() {

        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventTickets(eventId));
    }

    @Test
    void shouldReturnEventTicketsSuccessfully() throws ResourceNotFoundException {

        Long eventId = 1L;
        Event event = Event.builder().eventId(eventId).build();
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(Ticket.builder().ticketId(1L).build());

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(ticketRepository.findAllByEvent(event)).thenReturn(tickets);

        ResponseEntity<List<Ticket>> response = eventService.getEventTickets(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tickets, response.getBody());
    }

    @Test
    void shouldViewEventSuccessfully() {
        Long eventId = 1L;
        Event event = Event.builder()
                .eventId(eventId)
                .eventDescription("Sample Event")
                .category(CATEGORY.CONCERT)
                .startDateTime(new Date())
                .endDateTime(new Date(System.currentTimeMillis() + 86400000))
                .tickets(new ArrayList<>())
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        ResponseEntity<EventDTO> response = eventService.viewEvent(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(event.getEventDescription(), response.getBody().getEventDescription());
    }

    @Test
    void shouldReturnNotFoundWhenEventDoesNotExist() {
        Long eventId = 1L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<EventDTO> response = eventService.viewEvent(eventId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    void shouldSearchEventsSuccessfully() {
        Date startDate = new Date();
        CATEGORY category = CATEGORY.CONCERT;
        List<Event> events = new ArrayList<>();
        events.add(Event.builder().eventId(1L).eventDescription("Sample Event").build());

        when(eventRepository.findByStartDateTimeOrCategory(startDate, category)).thenReturn(events);

        Page<SearchResultDTO> result = eventService.searchEvents(startDate, CATEGORY.valueOf(category.toString()), PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

}

