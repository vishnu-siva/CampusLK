package lk.campuslk.bookmarks.dto;

import java.time.Instant;

public record BookmarkResponse(
        String id,
        String opportunityId,
        String opportunityTitle,
        Instant createdAt
) {}
