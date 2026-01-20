package lk.campuslk.notifications.impl;

import lk.campuslk.db.entity.Notification;
import lk.campuslk.db.repo.NotificationRepo;
import lk.campuslk.notifications.NotificationService;
import lk.campuslk.notifications.dto.NotificationDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo repository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(NotificationRepo repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional
    public NotificationDto createNotification(NotificationDto dto) {
        Notification n = new Notification();
        n.setTitle(dto.getTitle());
        n.setBody(dto.getBody());
        n.setCategory(dto.getCategory());
        n.setTargetStudentId(dto.getTargetStudentId());
        n.setRead(dto.isRead());
        Notification saved = repository.save(n);

        NotificationDto out = toDto(saved);
        String destination = "/topic/notifications/" + saved.getTargetStudentId();
        try {
            messagingTemplate.convertAndSend(destination, out);
        } catch (Exception ignored) {
            // don't fail saving when websocket isn't configured or no clients connected
        }
        return out;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getUnreadForStudent(String studentId) {
        return repository.findByTargetStudentIdAndIsReadFalseOrderByCreatedAtDesc(studentId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getAllForStudent(String studentId) {
        return repository.findByTargetStudentIdOrderByCreatedAtDesc(studentId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(String notificationId) {
        repository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            repository.save(n);
        });
    }

    private NotificationDto toDto(Notification n) {
        NotificationDto d = new NotificationDto();
        d.setId(n.getId());
        d.setTitle(n.getTitle());
        d.setBody(n.getBody());
        d.setCategory(n.getCategory());
        d.setTargetStudentId(n.getTargetStudentId());
        d.setRead(n.isRead());
        d.setCreatedAt(n.getCreatedAt());
        return d;
    }
}

