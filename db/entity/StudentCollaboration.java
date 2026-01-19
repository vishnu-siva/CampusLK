// campuslk-api/src/main/java/lk/campuslk/db/entity/StudentCollaboration.java
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "student_collaborations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "collaboration_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentCollaboration {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaboration_id", nullable = false)
    private Collaboration collaboration;

    @Column(length = 100)
    private String role;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;
}
