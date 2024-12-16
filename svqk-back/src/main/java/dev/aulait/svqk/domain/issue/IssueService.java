package dev.aulait.svqk.domain.issue;

import dev.aulait.svqk.arch.jpa.SearchUtils;
import dev.aulait.svqk.arch.search.SearchCriteriaVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class IssueService {

  private final IssueRepository repository;
  private final EntityManager em;

  private final JournalService journalService;

  @Transactional
  public IssueEntity save(IssueEntity entity) { // <.>
    return repository.save(entity); // <.>
  }

  @Transactional
  public IssueEntity update(IssueEntity issue, JournalEntity journal) {
    IssueEntity updatedEntity = repository.save(issue);
    journalService.save(journal);

    return updatedEntity;
  }

  public SearchResultVo<IssueEntity> search(SearchCriteriaVo criteria) { // <.>
    return SearchUtils.search(em, criteria); // <.>
  }

  public List<IssueTrackingRs> getTracking() {
    return repository.count4tracking();
  }

  public IssueEntity find(int id) {
    return repository.findByIdWithDetails(id, em);
  }
}
