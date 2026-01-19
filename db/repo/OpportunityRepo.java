// campuslk-api/src/main/java/lk/campuslk/db/repo/OpportunityRepo.java
package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpportunityRepo extends JpaRepository<Opportunity, String> {
    List<Opportunity> findByType(String type);
}
