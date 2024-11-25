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
import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
@RequiredArgsConstructor
public class IssueService {

  private final IssueRepository repository;
  private final JournalRepository journalRepository;
  private final EntityManager em;

  @Transactional
  public IssueEntity save(IssueEntity issueEntity, JournalEntity journalEntity) { // <.>
    IssueEntity savedEntity = repository.save(issueEntity); // <.>
    // TODO change if exp
    if (StringUtils.isNotBlank(journalEntity.getNotes())) {
      journalRepository.save(journalEntity);
    }

    return savedEntity;
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
}
