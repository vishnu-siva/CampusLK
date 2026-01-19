package lk.campuslk.collaborations.dto;

import java.time.Instant;

public record CollaborationRequestResponse(
        String id,
        String projectId,
        String projectTitle,
        String fromStudentId,
        String fromStudentName,
        String toStudentId,
        String status,
        Instant createdAt


) {}
