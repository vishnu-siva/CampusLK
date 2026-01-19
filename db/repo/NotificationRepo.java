// ...existing code...
package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, String> {
    List<Notification> findByTargetStudentIdOrderByCreatedAtDesc(String targetStudentId);
    List<Notification> findByTargetStudentIdIsNullOrderByCreatedAtDesc();
}

