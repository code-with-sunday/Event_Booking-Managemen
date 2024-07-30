package dev.sunday.DTO.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EmailDTO {
    private String to;
    private String subject;
    private String message;
}
