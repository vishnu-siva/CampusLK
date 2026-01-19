// ...existing code...
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    // e.g., "OPPORTUNITY" or "DEADLINE"
    private String category;

    // If notification targets a specific student, store studentId, otherwise null for broadcast
    @Column(name = "target_student_id")
    private String targetStudentId;

    @Column(name = "is_read")
    @Builder.Default
    private boolean read = false;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}

