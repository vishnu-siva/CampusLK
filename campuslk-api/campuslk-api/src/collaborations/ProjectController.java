package lk.campuslk.collaborations;

import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.Project;
import lk.campuslk.db.repo.ProjectRepo;
import lk.campuslk.collaborations.dto.ProjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepo projectRepo;

    // ✅ GET all projects
    @GetMapping
    public ApiResponse<List<Project>> all() {
        return new ApiResponse<>(true, "OK", projectRepo.findAll());
    }

    // ✅ POST create project
    @PostMapping
    public ApiResponse<Project> create(Authentication auth, @RequestBody ProjectRequest req) {
        String studentId = auth.getName(); // from JWT

        Project p = Project.builder()
                .id(UUID.randomUUID().toString())
                .title(req.title())
                .description(req.description())
                .createdAt(Instant.now())
                // createdBy is optional; if your entity has createdBy as Student, you can set it in a service.
                .build();

        // If your Project entity has postedBy/createdBy as String, you can do: p.setPostedBy(studentId)
        // If it has createdBy as Student, we need StudentRepo to fetch and set it.

        return new ApiResponse<>(true, "Project created", projectRepo.save(p));
    }
}
