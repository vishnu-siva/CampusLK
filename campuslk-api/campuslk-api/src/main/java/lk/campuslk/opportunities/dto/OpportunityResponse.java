package lk.campuslk.opportunities.dto;

import java.time.Instant;

public record OpportunityResponse(
        String id,
        String title,
        String description,
        String type,
        String fieldOfStudy,
        String interests,
        String postedById,
        Instant createdAt
) {}
