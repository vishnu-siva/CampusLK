package lk.campuslk.notifications.dto;

import java.time.LocalDateTime;

public class NotificationDto {
    private String id;
    private String title;
    private String body;
    private String category;
    private String targetStudentId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTargetStudentId() { return targetStudentId; }
    public void setTargetStudentId(String targetStudentId) { this.targetStudentId = targetStudentId; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

