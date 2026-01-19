package lk.campuslk.forum;

import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lk.campuslk.forum.dto.CommentResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/threads")
@RequiredArgsConstructor
public class CommentController {

    private final ForumService service;

    // ✅ GET /api/threads/{threadId}/comments
    @GetMapping("/{threadId}/comments")
    public ApiResponse<List<CommentResponse>> listComments(@PathVariable String threadId) {
        return new ApiResponse<>(true, "OK", service.listComments(threadId));
    }


    // ✅ POST /api/threads/{threadId}/comments
    @PostMapping("/{threadId}/comments")
    public ApiResponse<Void> addComment(
            Authentication auth,
            @PathVariable String threadId,
            @RequestBody Map<String, String> body
    ) {
        String content = body.get("content");
        service.addComment(auth.getName(), threadId, content);
        return new ApiResponse<>(true, "Comment added", null);
    }
}
