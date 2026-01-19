package lk.campuslk.students.dto;

public record StudentUpdateRequest(
        String name,
        String fieldOfStudy,
        String interests,
        String bio,
        String portfolioUrl,
        String notificationPreferences,
        String universityId
) {}
