package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, String> {

    List<Project> findByUniversityId(String universityId);

    List<Project> findByCreatedById(String studentId);
}
