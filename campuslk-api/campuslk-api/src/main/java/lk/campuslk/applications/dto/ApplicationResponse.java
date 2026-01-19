package lk.campuslk.applications.dto;

import java.time.Instant;

public record ApplicationResponse(
        String id,
        String opportunityId,
        String opportunityTitle,
        String status,
        Instant submittedAt,
        String resumeUrl,
        String coverLetterUrl,
        String notes
) {}
