package lk.campuslk.forum;

import lk.campuslk.db.entity.Comment;
import lk.campuslk.db.entity.Forum;
import lk.campuslk.db.entity.ForumThread;
import lk.campuslk.db.entity.Student;
import lk.campuslk.db.repo.CommentRepo;
import lk.campuslk.db.repo.ForumRepo;
import lk.campuslk.db.repo.StudentRepo;
import lk.campuslk.db.repo.ThreadRepo;
import lk.campuslk.forum.dto.CommentResponse;
import lk.campuslk.forum.dto.ForumThreadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepo forumRepo;
    private final ThreadRepo threadRepo;
    private final CommentRepo commentRepo;
    private final StudentRepo studentRepo;

    // ✅ Forums list
    public List<Forum> listForums() {
        return forumRepo.findAll();
    }

    // ✅ Threads list (DTO avoids lazy proxy errors)
    public List<ForumThreadResponse> listThreads(String forumId) {
        return threadRepo.findThreads(forumId).stream()
                .map(t -> new ForumThreadResponse(
                        t.getId(),
                        t.getForum().getId(),
                        t.getTitle(),
                        t.getCreatedBy().getId(),
                        t.getCreatedBy().getName(),
                        t.getCreatedAt()
                ))
                .toList();
    }

    // ✅ Create thread + save content as first comment
    public void createThread(String studentId, String forumId, String title, String content) {
        if (title == null || title.isBlank()) throw new RuntimeException("Title is required");
        if (content == null || content.isBlank()) throw new RuntimeException("Content is required");

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Forum forum = forumRepo.findById(forumId)
                .orElseThrow(() -> new RuntimeException("Forum not found"));

        ForumThread thread = ForumThread.builder()
                .id(UUID.randomUUID().toString())
                .forum(forum)
                .title(title)
                .createdBy(student) // ✅ createdBy is Student
                .createdAt(Instant.now())
                .build();

        threadRepo.save(thread);

        Comment first = Comment.builder()
                .id(UUID.randomUUID().toString())
                .thread(thread)
                .student(student)
                .content(content)
                .createdAt(Instant.now())
                .build();

        commentRepo.save(first);
    }

    // ✅ Comments list (DTO avoids lazy proxy errors)
    public List<CommentResponse> listComments(String threadId) {
        return commentRepo.findComments(threadId).stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        c.getThread().getId(),
                        c.getStudent().getId(),
                        c.getStudent().getName(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .toList();
    }

    // ✅ Add a reply
    public void addComment(String studentId, String threadId, String content) {
        if (content == null || content.isBlank()) throw new RuntimeException("Content is required");

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ForumThread thread = threadRepo.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Thread not found"));

        Comment c = Comment.builder()
                .id(UUID.randomUUID().toString())
                .thread(thread)
                .student(student)
                .content(content)
                .createdAt(Instant.now())
                .build();

        commentRepo.save(c);
    }
}
