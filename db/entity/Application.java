// campuslk-api/src/main/java/lk/campuslk/db/entity/Application.java
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","opportunity_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Application {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="opportunity_id")
    private Opportunity opportunity;

    @Column(nullable = false)
    private String status; // draft/submitted/accepted/rejected

    private Instant submittedAt;
    private Instant updatedAt;

    private String resumeUrl;
    private String coverLetterUrl;

    @Column(length = 2000)
    private String notes;
}
