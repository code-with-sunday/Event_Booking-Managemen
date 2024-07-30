package dev.sunday.model;

import dev.sunday.enums.CATEGORY;
import dev.sunday.enums.ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Event extends AuditBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String eventDescription;

    @Enumerated(EnumType.STRING)
    private CATEGORY category;
    private Integer ticketSold;
    private Integer availableAttendeesCount;
    private Date startDateTime;
    private Date endDateTime;
    private ROLE role;
    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets = new ArrayList<>();

}