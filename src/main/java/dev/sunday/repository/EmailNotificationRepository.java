package dev.sunday.repository;

import dev.sunday.model.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
}
