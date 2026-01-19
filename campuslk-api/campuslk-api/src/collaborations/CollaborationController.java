package lk.campuslk.collaborations;

import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.Collaboration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collaborations")
@RequiredArgsConstructor
public class CollaborationController {

    private final CollaborationService service;

    @PostMapping
    public ApiResponse<Collaboration> create(@RequestBody Collaboration c) {
        return new ApiResponse<>(true, "Created", service.save(c));
    }
}
