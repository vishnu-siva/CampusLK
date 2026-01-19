// campuslk-api/src/main/java/lk/campuslk/db/entity/ForumThread.java
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "threads")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ForumThread {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id", nullable = false)
    private Forum forum;

    @Column(nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Student createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
