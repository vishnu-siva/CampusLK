package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeadlineRepo extends JpaRepository<Deadline, String> {

    List<Deadline> findByOpportunityId(String opportunityId);

    List<Deadline> findByReminderSentFalse();
}
