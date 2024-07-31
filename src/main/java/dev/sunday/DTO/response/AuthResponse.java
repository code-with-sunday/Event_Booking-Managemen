package dev.sunday.DTO.response;

import dev.sunday.enums.ROLE;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private String Title;
    private String message;
    private ROLE role;
}