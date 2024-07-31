package dev.sunday.DTO.request;

import dev.sunday.enums.CATEGORY;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchResultDTO {
    private Long eventId;
    private String eventDescription;
    private CATEGORY category;
    private Date startDateTime;
    private Date endDateTime;
}
