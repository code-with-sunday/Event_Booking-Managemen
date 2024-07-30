package dev.sunday.DTO.request;

import dev.sunday.enums.CATEGORY;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SearchResultDTO {
    private Long eventId;
    private String eventDescription;
    private CATEGORY category;
    private Date startDateTime;
    private Date endDateTime;
}
