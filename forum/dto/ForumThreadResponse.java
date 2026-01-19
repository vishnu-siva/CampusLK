package lk.campuslk.forum.dto;

import java.time.Instant;

public record ForumThreadResponse(
        String id,
        String title,
        String content,
        String createdById,
        String createdByName,
        Instant createdAt
) {}
