package lk.campuslk.db.repo;

import lk.campuslk.db.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepo extends JpaRepository<University, String> {
}
