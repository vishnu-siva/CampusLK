package lk.campuslk.applications.dto;

public record ApplicationRequest(
        String opportunityId,
        String resumeUrl,
        String coverLetterUrl
) {}
