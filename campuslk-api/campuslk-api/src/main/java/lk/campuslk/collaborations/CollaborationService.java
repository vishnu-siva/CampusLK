package lk.campuslk.collaborations;

import lk.campuslk.collaborations.dto.CollaborationRequestResponse;
import lk.campuslk.db.entity.CollaborationRequest;
import lk.campuslk.db.entity.Project;
import lk.campuslk.db.entity.Student;
import lk.campuslk.db.repo.CollaborationRequestRepo;
import lk.campuslk.db.repo.ProjectRepo;
import lk.campuslk.db.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lk.campuslk.db.entity.Collaboration;
import lk.campuslk.db.repo.CollaborationRepo;

@Service
@RequiredArgsConstructor
public class CollaborationService {

    private final CollaborationRequestRepo requestRepo;
    private final StudentRepo studentRepo;
    private final ProjectRepo projectRepo;
    private final CollaborationRepo collaborationRepo;

    // ===============================
    // SEND COLLABORATION REQUEST
    // ===============================
    public CollaborationRequest sendRequest(
            String fromStudentId,
            String toStudentId,
            String projectId
    ) {
        Student from = studentRepo.findById(fromStudentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Student to = studentRepo.findById(toStudentId)
                .orElseThrow(() -> new RuntimeException("Target student not found"));

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        CollaborationRequest req = CollaborationRequest.builder()
                .id(UUID.randomUUID().toString())
                .project(project)
                .fromStudent(from)
                .toStudent(to)
                .status("pending")
                .createdAt(Instant.now())
                .sentAt(Instant.now())
                .build();

        return requestRepo.save(req);
    }

    // ===============================
    // INBOX (REQUESTS SENT TO ME)
    // ===============================
    public List<CollaborationRequestResponse> inbox(String studentId) {
        return requestRepo.inbox(studentId).stream()
                .map(r -> new CollaborationRequestResponse(
                        r.getId(),
                        r.getProject().getId(),
                        r.getProject().getTitle(),
                        r.getFromStudent().getId(),
                        r.getFromStudent().getName(),
                        r.getToStudent().getId(),
                        r.getStatus(),
                        r.getCreatedAt()
                ))
                .toList();
    }

    // ===============================
    // SENT (REQUESTS I SENT)
    // ===============================
    public List<CollaborationRequestResponse> sent(String studentId) {
        return requestRepo.sent(studentId).stream()
                .map(r -> new CollaborationRequestResponse(
                        r.getId(),
                        r.getProject().getId(),
                        r.getProject().getTitle(),
                        r.getFromStudent().getId(),
                        r.getFromStudent().getName(),
                        r.getToStudent().getId(),
                        r.getStatus(),
                        r.getCreatedAt()
                ))
                .toList();
    }

    public Collaboration save(Collaboration c) {
        c.setId(UUID.randomUUID().toString());
        if (c.getCreatedAt() == null) {
            c.setCreatedAt(Instant.now());
        }
        return collaborationRepo.save(c);
    }


}
