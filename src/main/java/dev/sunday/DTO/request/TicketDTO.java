package dev.sunday.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {
    private String ticketName;
    private double cost;
    private int totalQuantity;
    private int quantitySold=0;
    private int quantityLeft;
}
