package lk.campuslk.bookmarks;

import lk.campuslk.bookmarks.dto.BookmarkResponse;
import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService service;

    @PostMapping("/{opportunityId}")
    public ApiResponse<Void> bookmark(Authentication auth, @PathVariable String opportunityId) {
        String studentId = auth.getName();
        service.create(studentId, opportunityId);
        return new ApiResponse<>(true, "Bookmarked", null);
    }


    @GetMapping
    public ApiResponse<List<BookmarkResponse>> myBookmarks(Authentication auth) {
        String studentId = auth.getName();
        return new ApiResponse<>(true, "OK", service.listByStudent(studentId));
    }

    @DeleteMapping("/{opportunityId}")
    public ApiResponse<Void> unbookmark(
            org.springframework.security.core.Authentication auth,
            @PathVariable String opportunityId
    ) {
        service.remove(auth.getName(), opportunityId);
        return new ApiResponse<>(true, "Removed", null);
    }


}
