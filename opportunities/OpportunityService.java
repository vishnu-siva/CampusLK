package lk.campuslk.opportunities;

import lk.campuslk.db.entity.Opportunity;
import lk.campuslk.db.repo.OpportunityRepo;
import lk.campuslk.notifications.NotificationService;
import lk.campuslk.db.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OpportunityService {

    private final OpportunityRepo repo;
    private final NotificationService notificationService;

    public List<Opportunity> findAllEntities() {
        return repo.findAll();
    }

    public Opportunity create(Opportunity o) {
        Opportunity saved = repo.save(o);

        // publish a broadcast notification about the new opportunity
        Notification n = Notification.builder()
                .id(UUID.randomUUID().toString())
                .title("New opportunity: " + (saved.getTitle() == null ? "" : saved.getTitle()))
                .body(saved.getDescription())
                .category("OPPORTUNITY")
                .targetStudentId(null)
                .read(false)
                .build();
        notificationService.publish(n);

        return saved;
    }
}
