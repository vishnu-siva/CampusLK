package lk.campuslk.bookmarks;

import lk.campuslk.bookmarks.dto.BookmarkResponse;
import lk.campuslk.db.entity.Bookmark;
import lk.campuslk.db.entity.Opportunity;
import lk.campuslk.db.entity.Student;
import lk.campuslk.db.repo.BookmarkRepo;
import lk.campuslk.db.repo.OpportunityRepo;
import lk.campuslk.db.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepo bookmarkRepo;
    private final StudentRepo studentRepo;
    private final OpportunityRepo opportunityRepo;

    public Bookmark create(String studentId, String opportunityId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Opportunity opportunity = opportunityRepo.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        bookmarkRepo.findByStudentIdAndOpportunityId(studentId, opportunityId)
                .ifPresent(b -> { throw new RuntimeException("Already bookmarked"); });

        Bookmark b = Bookmark.builder()
                .id(UUID.randomUUID().toString())
                .student(student)
                .opportunity(opportunity)
                .createdAt(Instant.now())
                .build();

        return bookmarkRepo.save(b);
    }

    // ✅ SAFE LIST (no lazy proxy issue)
    public List<BookmarkResponse> listByStudent(String studentId) {
        return bookmarkRepo.findByStudentIdWithOpportunity(studentId).stream()
                .map(b -> new BookmarkResponse(
                        b.getId(),
                        b.getOpportunity().getId(),
                        b.getOpportunity().getTitle(),
                        b.getCreatedAt()
                ))
                .toList();
    }

    public void remove(String studentId, String opportunityId) {
        var bookmark = bookmarkRepo.findByStudentIdAndOpportunityId(studentId, opportunityId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        bookmarkRepo.delete(bookmark);
    }

}
