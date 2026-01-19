package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollaborationRepo extends JpaRepository<Collaboration, String> {

    Optional<Collaboration> findByProjectId(String projectId);
}
