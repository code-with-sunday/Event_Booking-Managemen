package dev.sunday.service;

import dev.sunday.DTO.request.EventDTO;
import dev.sunday.DTO.request.SearchResultDTO;
import dev.sunday.enums.CATEGORY;
import dev.sunday.exception.ResourceNotFoundException;
import dev.sunday.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface EventServices {
    ResponseEntity<EventDTO> saveEvent(EventDTO eventDto);

    ResponseEntity<String> publishEvent(Long eventId);

    Page<SearchResultDTO> searchEvents(Date startDate, CATEGORY category, Pageable pageable);

    ResponseEntity<List<Ticket>> getEventTickets(Long id) throws ResourceNotFoundException;

    ResponseEntity<String> deleteEvent(Long id);

    ResponseEntity<EventDTO> viewEvent(Long id);
}

