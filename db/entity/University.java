// campuslk-api/src/main/java/lk/campuslk/db/entity/University.java
package lk.campuslk.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "universities")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class University {
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String name;

    private String address;
    private String website;
    private String contactEmail;

    private Instant createdAt;
}
