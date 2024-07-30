package dev.sunday.DTO.response;

import dev.sunday.enums.ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String Title;
    private String message;
    private ROLE role;
}