package dev.sunday.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass
public abstract class AuditBaseEntity {
    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditBaseEntity that = (AuditBaseEntity) o;
        return Objects.equals(createDate, that.createDate) &&
                Objects.equals(updateDate, that.updateDate);
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (createDate == null) {
            createDate = LocalDateTime.now();
        }
        updateDate = LocalDateTime.now();
    }

    @Override
    public int hashCode() {
        return Objects.hash(createDate, updateDate);
    }
}