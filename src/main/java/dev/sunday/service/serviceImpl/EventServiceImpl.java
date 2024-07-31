package dev.sunday.service.serviceImpl;

import dev.sunday.DTO.request.EventDTO;
import dev.sunday.DTO.request.SearchResultDTO;
import dev.sunday.DTO.request.TicketDTO;
import dev.sunday.enums.CATEGORY;
import dev.sunday.exception.RequestedResourceNotFoundException;
import dev.sunday.exception.ResourceNotFoundException;
import dev.sunday.exception.UnAuthorizedException;
import dev.sunday.model.Event;
import dev.sunday.model.Ticket;
import dev.sunday.model.User;
import dev.sunday.repository.EventRepository;
import dev.sunday.repository.TicketRepository;
import dev.sunday.repository.UserRepository;
import dev.sunday.service.EventServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventServices {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    @Override
    public ResponseEntity<EventDTO> saveEvent(EventDTO eventDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            String email = (String) authentication.getPrincipal();

            User user = userRepository.findByEmail(email);

            Event event = new Event();
            event.setEventDescription(eventDto.getEventDescription());
            event.setRole(user.getRole());
            event.setAvailableAttendeesCount(eventDto.getAvailableAttendeesCount());
            event.setCategory(eventDto.getCategory());
            event.setStartDateTime(eventDto.getStartDateTime());
            event.setEndDateTime(eventDto.getEndDateTime());
            event.setUser(user);

            Event savedEvent = eventRepository.save(event);
            savedEvent.getEventId();
            Event savedEvent2 = savedEvent.builder()
                    .eventId(savedEvent.getEventId())
                    .eventDescription(savedEvent.getEventDescription())
                    .role(savedEvent.getRole())
                    .availableAttendeesCount(savedEvent.getAvailableAttendeesCount())
                    .category(savedEvent.getCategory())
                    .startDateTime(savedEvent.getStartDateTime())
                    .endDateTime(savedEvent.getEndDateTime())
                    .user(savedEvent.getUser())
                    .build();
            log.info("Saved event: {}", savedEvent2);

            List<Ticket> ticketsSpecification = eventDto.getTicketsInfo()
                    .stream()
                    .map(this::ticketDto2Ticket)
                    .collect(Collectors.toList());

            for (Ticket ticket : ticketsSpecification) {
                ticket.setEvent(savedEvent);
            }

            List<Ticket> savedTickets = ticketRepository.saveAll(ticketsSpecification);
            log.info("savedTickets with associated event saved successfully: {}", savedTickets);

            EventDTO eventDto1 = EventToEventDTo(savedEvent2);
            eventDto1.setTicketsInfo(savedTickets.stream().map(this::ticket2TicketDto).collect(Collectors.toList()));

            publishEvent(savedEvent2.getEventId());

            return new ResponseEntity<>(eventDto1, HttpStatus.OK);

        } else {
            throw new RuntimeException("User not authenticated");
        }
    }

    @Override
    public ResponseEntity<String> publishEvent(Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        log.info("he principal: {}", authentication.getPrincipal());
        User user = userRepository.findByEmail(authentication.getPrincipal().toString());

        try {
            Optional<Event> eventOptional = eventRepository.findById(eventId);
            if (!eventOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
            }

            Event event = eventOptional.get();

            if (!user.getId().equals(event.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to publish this event");
            }

            eventRepository.save(event);

            return ResponseEntity.status(HttpStatus.OK).body("Event published successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to publish event");
        }
    }

    @Override
    public Page<SearchResultDTO> searchEvents(Date startDate, CATEGORY category, Pageable pageable) {

        List<Event> events = eventRepository.findByStartDateTimeOrCategory(startDate, category);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), events.size());
        Page<Event> eventPage = new PageImpl<>(events.subList(start, end), pageable, events.size());

        Page<SearchResultDTO> searchResult = eventPage.map(this::eventToSearchResultDto);

        return searchResult;
    }

    private SearchResultDTO eventToSearchResultDto(Event event) {
        return SearchResultDTO.builder()
                .eventId(event.getEventId())
                .eventDescription(event.getEventDescription())
                .category(event.getCategory())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .build();
    }

    @Override
    public ResponseEntity<List<Ticket>> getEventTickets(Long id) throws ResourceNotFoundException {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event with the id:" + id + " not found"));

        List<Ticket> tickets = ticketRepository.findAllByEvent(event);

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteEvent(Long id) {
        Optional<Event> event= eventRepository. findById(id);
        if(event.isEmpty()){
            throw new RequestedResourceNotFoundException("Event Not found");
        }
        Event event1 =event.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        if(user.getId()!= event1.getUser().getId()){
            throw new UnAuthorizedException("Access denied.You can't delete event you didn't create");
        }
        if(event1.getTicketSold()<1){
            ticketRepository.deleteAll(event1.getTickets());
            eventRepository.delete(event1);
            return new ResponseEntity<>("Event deleted successfully",HttpStatus.OK);
        }
        Date CurrentDate = new Date();
        if(CurrentDate.after(event1.getEndDateTime())){
            return  new ResponseEntity<>("Event has been deleted",HttpStatus.OK);
        }
        return  new ResponseEntity<>("Event cannot be deleted, till event date has passed",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventDTO> viewEvent(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            EventDTO eventDto = EventToEventDTo(event);
            return ResponseEntity.ok(eventDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    private Ticket ticketDto2Ticket(TicketDTO ticketDto) {
        return Ticket.builder()
                .ticketName(ticketDto.getTicketName())
                .cost(ticketDto.getCost())
                .totalQuantity(ticketDto.getTotalQuantity())
                .quantitySold(ticketDto.getQuantitySold())
                .quantityLeft(ticketDto.getQuantityLeft())
                .build();
    }

    private TicketDTO ticket2TicketDto(Ticket ticket) {
        return TicketDTO.builder()
                .ticketName(ticket.getTicketName())
                .cost(ticket.getCost())
                .totalQuantity(ticket.getTotalQuantity())
                .quantitySold(ticket.getQuantitySold())
                .quantityLeft(ticket.getQuantityLeft())
                .build();
    }

    private EventDTO EventToEventDTo(Event event) {
        return EventDTO.builder()
                .eventDescription(event.getEventDescription())
                .category(event.getCategory())
                .availableAttendeesCount(event.getAvailableAttendeesCount())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .ticketsInfo(event.getTickets().stream()
                        .map(this::ticket2TicketDto)
                        .collect(Collectors.toList()))
                .build();
    }

}

