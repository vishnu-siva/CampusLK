// ...existing code...
package lk.campuslk.notifications;

import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.Notification;
import lk.campuslk.notifications.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;

    @GetMapping
    public ApiResponse<List<Notification>> list(Authentication auth) {
        String studentId = (auth == null) ? null : auth.getName();
        return new ApiResponse<>(true, "OK", service.listForStudent(studentId));
    }

    @PostMapping
    public ApiResponse<Notification> create(@RequestBody NotificationRequest req) {
        Notification n = Notification.builder()
                .id(UUID.randomUUID().toString())
                .title(req.title())
                .body(req.body())
                .category(req.category())
                .targetStudentId(req.targetStudentId())
                .read(false)
                .build();
        service.publish(n);
        return new ApiResponse<>(true, "Notification created", n);
    }

    @PutMapping("/{id}/read")
    public ApiResponse<Notification> markRead(@PathVariable String id) {
        Optional<Notification> opt = service.markRead(id);
        return opt.map(n -> new ApiResponse<>(true, "Marked read", n))
                .orElseGet(() -> new ApiResponse<>(false, "Not found", null));
    }

    @GetMapping("/stream")
    public SseEmitter stream(@RequestParam(required = false) String studentId) {
        return service.subscribe(studentId);
    }
}

