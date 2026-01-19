package lk.campuslk.db.repo;

import lk.campuslk.db.entity.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepo extends JpaRepository<Forum, String> {}
