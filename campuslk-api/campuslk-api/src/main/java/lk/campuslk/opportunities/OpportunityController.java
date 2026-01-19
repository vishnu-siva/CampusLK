package lk.campuslk.opportunities;

import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.Opportunity;
import lk.campuslk.opportunities.dto.OpportunityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService service;

    @GetMapping
    public ApiResponse<List<Opportunity>> all() {
        return new ApiResponse<>(true, "OK", service.findAllEntities());
    }

    @PostMapping
    public ApiResponse<Opportunity> create(Authentication auth, @RequestBody OpportunityRequest req) {
        String studentId = auth.getName();

        Opportunity o = Opportunity.builder()
                .id(UUID.randomUUID().toString())
                .title(req.title())
                .description(req.description())
                .type(req.type())
                .fieldOfStudy(req.fieldOfStudy())
                .interests(req.interests())
                .postedBy(studentId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return new ApiResponse<>(true, "Opportunity created", service.create(o));
    }
}
