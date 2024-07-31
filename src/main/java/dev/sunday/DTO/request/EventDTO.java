package dev.sunday.DTO.request;

import dev.sunday.enums.CATEGORY;
import dev.sunday.enums.ROLE;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventDTO {
    private ROLE role;
    private String eventDescription;
    private CATEGORY category;
    private Integer availableAttendeesCount;
    private List<TicketDTO> ticketsInfo;
    private Date startDateTime;
    private Date endDateTime;
}
