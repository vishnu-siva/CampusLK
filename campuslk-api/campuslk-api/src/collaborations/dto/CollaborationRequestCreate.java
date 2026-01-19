package lk.campuslk.collaborations.dto;

public record CollaborationRequestCreate(
        String projectId,
        String toStudentId
) {}
