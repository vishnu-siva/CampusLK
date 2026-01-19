package lk.campuslk.forum;

import lk.campuslk.common.ApiResponse;
import lk.campuslk.forum.dto.ForumThreadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ThreadController {

    private final ForumService service;

    // ✅ LIST THREADS in a forum
    // GET /api/forums/{forumId}/threads
    @GetMapping("/forums/{forumId}/threads")
    public ApiResponse<List<ForumThreadResponse>> listThreads(@PathVariable String forumId) {
        return new ApiResponse<>(true, "OK", service.listThreads(forumId));
    }

    // ✅ CREATE THREAD
    // POST /api/forums/{forumId}/threads
    @PostMapping("/forums/{forumId}/threads")
    public ApiResponse<Void> createThread(
            Authentication auth,
            @PathVariable String forumId,
            @RequestBody Map<String, String> body
    ) {
        String title = body.get("title");
        String content = body.get("content");

        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            throw new RuntimeException("Title and content required");
        }

        service.createThread(auth.getName(), forumId, title, content);
        return new ApiResponse<>(true, "Thread created", null);
    }
}
