package lk.campuslk.applications;

import lk.campuslk.applications.dto.ApplicationResponse;
import lk.campuslk.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService service;

    @PostMapping
    public ApiResponse<Void> apply(Authentication auth, @RequestBody Map<String, Object> body) {
        String studentId = auth.getName();

        String opportunityId;
        if (body.containsKey("opportunity") && body.get("opportunity") != null) {
            Map<String, Object> opp = (Map<String, Object>) body.get("opportunity");
            opportunityId = opp.get("id").toString();
        } else if (body.containsKey("opportunityId") && body.get("opportunityId") != null) {
            opportunityId = body.get("opportunityId").toString();
        } else {
            throw new RuntimeException("Missing opportunityId");
        }

        String resumeUrl = body.get("resumeUrl") == null ? "" : body.get("resumeUrl").toString();
        String coverLetterUrl = body.get("coverLetterUrl") == null ? "" : body.get("coverLetterUrl").toString();
        String notes = body.get("notes") == null ? "" : body.get("notes").toString();

        if (resumeUrl.isBlank()) {
            throw new RuntimeException("resumeUrl is required");
        }

        service.apply(studentId, opportunityId, resumeUrl, coverLetterUrl, notes);
        return new ApiResponse<>(true, "Application submitted", null);
    }

    @GetMapping
    public ApiResponse<List<ApplicationResponse>> myApplications(Authentication auth) {
        return new ApiResponse<>(true, "OK", service.listMyApplications(auth.getName()));
    }

    @PostMapping("/{applicationId}/withdraw")
    public ApiResponse<Void> withdraw(Authentication auth, @PathVariable String applicationId) {
        service.withdraw(auth.getName(), applicationId);
        return new ApiResponse<>(true, "Application withdrawn", null);
    }

    @PutMapping("/{applicationId}/withdraw")
    public ApiResponse<Void> withdrawPut(Authentication auth, @PathVariable String applicationId) {
        service.withdraw(auth.getName(), applicationId);
        return new ApiResponse<>(true, "Application withdrawn", null);
    }

}
