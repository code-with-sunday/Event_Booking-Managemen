package dev.sunday.controller;

import dev.sunday.DTO.request.EventDTO;
import dev.sunday.DTO.request.SearchResultDTO;
import dev.sunday.enums.CATEGORY;
import dev.sunday.service.EventServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventController {
    private final EventServices eventServices;


    @PostMapping("/event/create")
    public ResponseEntity<EventDTO> create(@RequestBody EventDTO eventDto) {
        return eventServices.saveEvent(eventDto);
    }


    @PutMapping("/notification/{eventId}")
    public ResponseEntity<String> publishEvent(@PathVariable Long eventId) {
        return eventServices.publishEvent(eventId);
    }


    @GetMapping("/event/search")
    public ResponseEntity<Page<SearchResultDTO>> searchEvents(
            @RequestParam(required = false) String eventTitle,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, defaultValue = "All Category") CATEGORY category,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int pageSize) {
        int maxSize = 20;
        pageSize = Math.min(pageSize, maxSize);

        Date convertedStartDate = parseStartDate(startDate);

        Page<SearchResultDTO> events = eventServices.searchEvents(convertedStartDate,category,
                PageRequest.of(page, pageSize));
        return ResponseEntity.ok(events);
    }

    @GetMapping("/view/{eventId}")
    public ResponseEntity<EventDTO> viewEvent(@PathVariable Long eventId) {
        EventDTO eventDto = eventServices.viewEvent(eventId).getBody();
        if (eventDto != null) {
            return ResponseEntity.ok(eventDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/event/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        return eventServices.deleteEvent(id);
    }


    private Date parseStartDate(String startDateString) {
        if (startDateString != null && !startDateString.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(startDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}

