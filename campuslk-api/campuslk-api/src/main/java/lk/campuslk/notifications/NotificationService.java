package lk.campuslk.notifications;

import lk.campuslk.notifications.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    NotificationDto createNotification(NotificationDto dto);
    List<NotificationDto> getUnreadForStudent(String studentId);
    List<NotificationDto> getAllForStudent(String studentId);
    void markAsRead(String notificationId);
}

