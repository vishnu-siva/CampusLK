package lk.campuslk.db.repo;

import lk.campuslk.db.entity.CollaborationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollaborationRequestRepo
        extends JpaRepository<CollaborationRequest, String> {

    // Requests SENT TO ME
    @Query("""
      select cr from CollaborationRequest cr
      join fetch cr.project p
      join fetch cr.fromStudent fs
      where cr.toStudent.id = :studentId
      order by cr.createdAt desc
    """)
    List<CollaborationRequest> inbox(
            @Param("studentId") String studentId
    );

    // Requests I SENT
    @Query("""
      select cr from CollaborationRequest cr
      join fetch cr.project p
      join fetch cr.toStudent ts
      where cr.fromStudent.id = :studentId
      order by cr.createdAt desc
    """)
    List<CollaborationRequest> sent(
            @Param("studentId") String studentId
    );
}
