package lk.campuslk.db.repo;

import lk.campuslk.db.entity.StudentCollaboration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentCollaborationRepo
        extends JpaRepository<StudentCollaboration, String> {

    List<StudentCollaboration> findByStudentId(String studentId);

    List<StudentCollaboration> findByCollaborationId(String collaborationId);
}
