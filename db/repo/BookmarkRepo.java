package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepo extends JpaRepository<Bookmark, String> {

    Optional<Bookmark> findByStudentIdAndOpportunityId(String studentId, String opportunityId);

    // ✅ fetch opportunity in same query to avoid lazy proxy error
    @Query("""
    select b from Bookmark b
    join fetch b.opportunity o
    where b.student.id = :studentId
  """)
    List<Bookmark> findByStudentIdWithOpportunity(@Param("studentId") String studentId);
}
