package dev.sunday.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "ticket",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"event_id", "ticket"})})
public class Ticket{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "ticket", length = 20, nullable = false)
    private String ticketName;

    private double cost;
    private int totalQuantity;
    private int quantitySold = 0;
    private int quantityLeft;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false,
            foreignKey = @ForeignKey(name = "ticket_fk_event"))
    private Event event;

}
