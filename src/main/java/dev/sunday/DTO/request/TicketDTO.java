package dev.sunday.DTO.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketDTO {
    private String ticketName;
    private double cost;
    private int totalQuantity;
    private int quantitySold=0;
    private int quantityLeft;
}
