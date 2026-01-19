package lk.campuslk.forum.dto;

import java.time.Instant;

public record CommentResponse(
        String id,
        String threadId,
        String studentId,
        String studentName,
        String content,
        Instant createdAt
) {
}
