package lk.campuslk.forum;

import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.Forum;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forums")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService service;

    // ✅ GET /api/forums  (list forum categories)
    @GetMapping
    public ApiResponse<List<Forum>> listForums() {
        return new ApiResponse<>(true, "OK", service.listForums());
    }
}
