package dev.sunday.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_notification")
public class EmailNotification extends BaseEntity {
    @Column(name = "\"to\"")
    private String to;
    private String subject;
    private String message;
}
