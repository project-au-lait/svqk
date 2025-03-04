package dev.aulait.svqk.domain.issue;

import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchResult;
import dev.aulait.sqb.jpa.JpaSearchQueryExecutor;
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

  public IssueEntity find(int id) {
    return repository.findByIdWithDetails(id, em);
  }

  @Transactional
  public IssueEntity update(IssueEntity issue, JournalEntity journal) {
    IssueEntity updatedEntity = repository.save(issue);
    journalService.save(journal);

    return updatedEntity;
  }

  @Transactional
  public void delete(IssueEntity entity) {
    IssueEntity managedEntity = em.merge(entity);
    repository.delete(managedEntity);
  }

  public SearchResult<IssueEntity> search(SearchCriteria criteria) { // <.>
    JpaSearchQueryExecutor executor = new JpaSearchQueryExecutor();
    return executor.search(em, criteria); // <.>
  }

  public List<IssueTrackingRs> getTracking() {
    return repository.count4tracking();
  }
}
