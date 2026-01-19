// campuslk-api/src/main/java/lk/campuslk/db/entity/CollaborationRequest.java
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Column;

import java.time.Instant;

@Entity
@Table(name = "collaboration_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollaborationRequest {

    @Id
    private String id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Student fromStudent;

    @ManyToOne
    private Student toStudent;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant sentAt;

    private Instant respondedAt;
}

