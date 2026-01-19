package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepo extends JpaRepository<Application, String> {

    boolean existsByStudentIdAndOpportunityId(String studentId, String opportunityId);

    Optional<Application> findByIdAndStudentId(String id, String studentId);

    // ✅ join fetch prevents lazy proxy errors
    @Query("""
      select a from Application a
      join fetch a.opportunity o
      where a.student.id = :studentId
      order by a.submittedAt desc
    """)
    List<Application> findMyApplications(@Param("studentId") String studentId);
}
