package lk.campuslk.opportunities;

import lk.campuslk.db.entity.Opportunity;
import lk.campuslk.db.repo.OpportunityRepo;
import lk.campuslk.notifications.NotificationService;
import lk.campuslk.notifications.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // Automatic notification when a new opportunity is created.
        // Assumption: `postedBy` contains an identifier for the student/admin who should receive the creation notice.
        try {
            if (saved.getPostedBy() != null) {
                NotificationDto dto = new NotificationDto();
                dto.setTitle("New opportunity: " + (saved.getTitle() == null ? "" : saved.getTitle()));
                dto.setBody(saved.getDescription() == null ? "" : saved.getDescription());
                dto.setCategory("opportunity");
                dto.setTargetStudentId(saved.getPostedBy());
                notificationService.createNotification(dto);
            }
        } catch (Exception ignored) {
            // Avoid failing the main save operation if notification sending fails.
        }

        return saved;
    }
}
