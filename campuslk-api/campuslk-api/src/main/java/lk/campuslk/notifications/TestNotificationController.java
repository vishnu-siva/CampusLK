package lk.campuslk.notifications;

import lk.campuslk.notifications.dto.NotificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestNotificationController {

    private final NotificationService service;

    public TestNotificationController(NotificationService service) {
        this.service = service;
    }

    /**
     * Quick test endpoint (browser-friendly).
     * Example:
     *  /test/send/student-id-789?title=Campus%20Event%20Reminder&body=Tech%20Talk%20starts%20in%201%20hour&category=event
     */
    @GetMapping("/send/{studentId}")
    public ResponseEntity<NotificationDto> sendTest(
            @PathVariable String studentId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String body,
            @RequestParam(required = false) String category
    ) {
        NotificationDto dto = new NotificationDto();
        dto.setTitle(title != null && !title.isBlank() ? title : "Test notification");
        dto.setBody(body != null && !body.isBlank() ? body : ("This is a test notification for " + studentId));
        dto.setCategory(category != null && !category.isBlank() ? category : "test");
        dto.setTargetStudentId(studentId);
        dto.setRead(false);

        NotificationDto created = service.createNotification(dto);
        return ResponseEntity.ok(created);
    }

    /**
     * Custom test endpoint (Postman / app-friendly).
     * POST /test/send
     * {
     *   "title": "Campus Event Reminder",
     *   "body": "Tech Talk starts in 1 hour at Auditorium A",
     *   "category": "event",
     *   "targetStudentId": "student-id-789",
     *   "read": false
     * }
     */
    @PostMapping("/send")
    public ResponseEntity<NotificationDto> sendCustom(@RequestBody NotificationDto dto) {
        if (dto.getTargetStudentId() == null || dto.getTargetStudentId().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if (dto.getTitle() == null) dto.setTitle("");
        if (dto.getBody() == null) dto.setBody("");
        if (dto.getCategory() == null) dto.setCategory("test");
        dto.setRead(false);

        NotificationDto created = service.createNotification(dto);
        return ResponseEntity.ok(created);
    }
}
