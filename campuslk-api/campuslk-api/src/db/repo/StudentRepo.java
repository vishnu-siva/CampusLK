// campuslk-api/src/main/java/lk/campuslk/db/repo/StudentRepo.java
package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, String> {
    Optional<Student> findByEmail(String email);
}
