// campuslk-api/src/main/java/lk/campuslk/db/entity/Opportunity.java
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "opportunities")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Opportunity {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String description;

    @Column(nullable = false)
    private String type; // scholarship/internship/competition/project

    private String fieldOfStudy;
    private String interests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @Column(name="posted_by")
    private String postedBy;

    private Instant createdAt;
    private Instant updatedAt;
}
