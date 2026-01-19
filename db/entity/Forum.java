// campuslk-api/src/main/java/lk/campuslk/db/entity/Forum.java
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "forums")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Forum {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Student createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
