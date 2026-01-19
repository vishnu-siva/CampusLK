package lk.campuslk.opportunities.dto;

public record OpportunityRequest(
        String title,
        String description,
        String type,
        String fieldOfStudy,
        String interests
) {}
