package dev.aulait.svqk.domain.issue;

import static dev.aulait.svqk.arch.jpa.JpaRepositoryHandler.findByIdAsResource;

import dev.aulait.svqk.arch.jpa.SearchUtils;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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

  public IssueEntity find(int id) {
    return findByIdAsResource(repository, id);
  }

  public SearchResultVo<IssueEntity> search(SearchConditionVo condition) { // <.>
    return SearchUtils.search(em, condition); // <.>
  }

  public List<IssueTrackingRs> getTracking() {
    return repository.count4tracking();
  }

  public IssueEntity findIssueWithDetails(int id) {
    String jpql = "SELECT issue FROM IssueEntity issue "
        + "LEFT JOIN FETCH issue.issueStatus "
        + "LEFT JOIN FETCH issue.tracker "
        + "LEFT JOIN FETCH issue.journals "
        + "WHERE issue.id = :id";
    TypedQuery<IssueEntity> query = em.createQuery(jpql, IssueEntity.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }
}
