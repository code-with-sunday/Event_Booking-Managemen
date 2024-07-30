package dev.sunday.service;

import java.util.Map;

public interface TicketService {
    Map<String, Map<String, Integer>> findAllByEventId(Long eventId);
}
