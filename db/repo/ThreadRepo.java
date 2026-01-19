package lk.campuslk.db.repo;

import lk.campuslk.db.entity.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThreadRepo extends JpaRepository<ForumThread, String> {

    @Query("""
      select t from ForumThread t
      join fetch t.forum f
      join fetch t.createdBy cb
      where f.id = :forumId
      order by t.createdAt desc
    """)
    List<ForumThread> findThreads(@Param("forumId") String forumId);
}
