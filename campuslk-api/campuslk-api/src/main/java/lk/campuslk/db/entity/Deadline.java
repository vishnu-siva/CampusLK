// campuslk-api/src/main/java/lk/campuslk/db/entity/Deadline.java
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "deadlines")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Deadline {
    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunity opportunity;

    private LocalDate dueDate;

    private boolean reminderSent;
}
