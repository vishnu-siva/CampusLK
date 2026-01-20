package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, String> {
    List<Notification> findByTargetStudentIdAndIsReadFalseOrderByCreatedAtDesc(String targetStudentId);
    List<Notification> findByTargetStudentIdOrderByCreatedAtDesc(String targetStudentId);
}

