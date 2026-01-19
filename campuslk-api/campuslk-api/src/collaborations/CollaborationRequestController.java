package lk.campuslk.collaborations;

import lk.campuslk.collaborations.dto.CollaborationRequestCreate;
import lk.campuslk.common.ApiResponse;
import lk.campuslk.db.entity.CollaborationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collaboration-requests")
@RequiredArgsConstructor
public class CollaborationRequestController {

    private final CollaborationService service;

    // ===============================
    // SEND COLLABORATION REQUEST
    // ===============================
    @PostMapping
    public ApiResponse<?> send(
            Authentication auth,
            @RequestBody CollaborationRequestCreate req
    ) {
        String fromStudentId = auth.getName();

        CollaborationRequest saved = service.sendRequest(
                fromStudentId,
                req.toStudentId(),
                req.projectId()
        );

        return new ApiResponse<>(true, "Request sent", null);
    }

    // ===============================
    // INBOX
    // ===============================
    @GetMapping("/inbox")
    public ApiResponse<?> inbox(Authentication auth) {
        return new ApiResponse<>(
                true,
                "OK",
                service.inbox(auth.getName())
        );
    }

    // ===============================
    // SENT
    // ===============================
    @GetMapping("/sent")
    public ApiResponse<?> sent(Authentication auth) {
        return new ApiResponse<>(
                true,
                "OK",
                service.sent(auth.getName())
        );
    }
}
