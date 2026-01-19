// campuslk-api/src/main/java/lk/campuslk/db/entity/Collaboration.java
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "collaborations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Collaboration {


    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
