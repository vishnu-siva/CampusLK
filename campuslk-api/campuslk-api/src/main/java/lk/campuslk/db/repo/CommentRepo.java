package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, String> {

    @Query("""
      select c from Comment c
      join fetch c.thread t
      join fetch c.student s
      where t.id = :threadId
      order by c.createdAt asc
    """)
    List<Comment> findComments(@Param("threadId") String threadId);
}
