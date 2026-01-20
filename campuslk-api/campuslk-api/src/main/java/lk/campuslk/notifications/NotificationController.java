package lk.campuslk.notifications;

import lk.campuslk.notifications.dto.NotificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotificationDto> create(@RequestBody NotificationDto dto) {
        NotificationDto created = service.createNotification(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/unread/{studentId}")
    public ResponseEntity<List<NotificationDto>> unread(@PathVariable String studentId) {
        return ResponseEntity.ok(service.getUnreadForStudent(studentId));
    }

    @GetMapping("/all/{studentId}")
    public ResponseEntity<List<NotificationDto>> all(@PathVariable String studentId) {
        return ResponseEntity.ok(service.getAllForStudent(studentId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable String id) {
        service.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}

