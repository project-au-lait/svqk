package dev.aulait.svqk.domain.issue;

import static dev.aulait.svqk.arch.jpa.JpaRepositoryHandler.findByIdAsResource;

import dev.aulait.svqk.arch.jpa.SearchUtils;
import dev.aulait.svqk.arch.search.SearchConditionVo;
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

  public IssueEntity find(int id) {
    return repository.findIssueWithDetailsById(id);
  }

  public SearchResultVo<IssueEntity> search(SearchConditionVo condition) { // <.>
    return SearchUtils.search(em, condition); // <.>
  }

  public List<IssueTrackingRs> getTracking() {
    return repository.count4tracking();
  }
}
