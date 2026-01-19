package lk.campuslk.opportunities;

import lk.campuslk.db.entity.Opportunity;
import lk.campuslk.db.repo.OpportunityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpportunityService {

    private final OpportunityRepo repo;

    public List<Opportunity> findAllEntities() {
        return repo.findAll();
    }

    public Opportunity create(Opportunity o) {
        return repo.save(o);
    }
}
