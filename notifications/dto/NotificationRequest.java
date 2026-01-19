// ...existing code...
package lk.campuslk.notifications.dto;

public record NotificationRequest(
        String title,
        String body,
        String category,
        String targetStudentId
) {}

