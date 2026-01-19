package lk.campuslk.students;

import lk.campuslk.db.entity.Student;
import lk.campuslk.db.entity.University;
import lk.campuslk.db.repo.StudentRepo;
import lk.campuslk.db.repo.UniversityRepo;
import lk.campuslk.students.dto.StudentUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepo studentRepo;
    private final UniversityRepo universityRepo;

    public Student getById(String studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student update(String studentId, StudentUpdateRequest req) {
        Student student = getById(studentId);

        if (req.name() != null)
            student.setName(req.name());

        if (req.fieldOfStudy() != null)
            student.setFieldOfStudy(req.fieldOfStudy());

        if (req.interests() != null)
            student.setInterests(req.interests());

        if (req.bio() != null)
            student.setBio(req.bio());

        if (req.portfolioUrl() != null)
            student.setPortfolioUrl(req.portfolioUrl());

        if (req.notificationPreferences() != null)
            student.setNotificationPreferences(req.notificationPreferences());

        if (req.universityId() != null) {
            University uni = universityRepo.findById(req.universityId())
                    .orElseThrow(() -> new RuntimeException("University not found"));
            student.setUniversity(uni);
        }

        student.setUpdatedAt(Instant.now());
        return studentRepo.save(student);
    }
}
