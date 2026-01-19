package lk.campuslk.applications;

import lk.campuslk.applications.dto.ApplicationResponse;
import lk.campuslk.db.entity.Application;
import lk.campuslk.db.entity.Opportunity;
import lk.campuslk.db.entity.Student;
import lk.campuslk.db.repo.ApplicationRepo;
import lk.campuslk.db.repo.OpportunityRepo;
import lk.campuslk.db.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepo applicationRepo;
    private final StudentRepo studentRepo;
    private final OpportunityRepo opportunityRepo;

    // ✅ Apply with 5 arguments (matches your controller)
    public void apply(String studentId, String opportunityId, String resumeUrl, String coverLetterUrl, String notes) {

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Opportunity opp = opportunityRepo.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        // prevent duplicate apply
        if (applicationRepo.existsByStudentIdAndOpportunityId(studentId, opportunityId)) {
            throw new RuntimeException("Already applied");
        }

        Application app = Application.builder()
                .id(UUID.randomUUID().toString())
                .student(student)
                .opportunity(opp)
                .status("submitted")
                .submittedAt(Instant.now())
                .updatedAt(Instant.now())
                .resumeUrl(resumeUrl)
                .coverLetterUrl(coverLetterUrl)
                .notes(notes)
                .build();

        applicationRepo.save(app);
    }

    // ✅ This is what controller calls now
    public List<ApplicationResponse> listMyApplications(String studentId) {
        return applicationRepo.findMyApplications(studentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public void withdraw(String studentId, String applicationId) {
        Application app = applicationRepo.findByIdAndStudentId(applicationId, studentId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if ("withdrawn".equalsIgnoreCase(app.getStatus())) {
            throw new RuntimeException("Already withdrawn");
        }

        app.setStatus("withdrawn");
        app.setUpdatedAt(Instant.now());
        applicationRepo.save(app);
    }

    private ApplicationResponse toResponse(Application a) {
        return new ApplicationResponse(
                a.getId(),
                a.getOpportunity().getId(),
                a.getOpportunity().getTitle(),
                a.getStatus(),
                a.getSubmittedAt(),
                a.getResumeUrl(),
                a.getCoverLetterUrl(),
                a.getNotes()
        );
    }
}
