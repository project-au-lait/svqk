package dev.aulait.svqk.domain.issue;

import dev.aulait.svqk.arch.jpa.SearchUtils;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public SearchResultVo<IssueEntity> search(SearchConditionVo condition) { // <.>
    return SearchUtils.search(em, condition); // <.>
  }

  public List<IssueTrackingRs> getTracking() {
    return repository.count4tracking();
  }

  public IssueEntity findIssueWithDetails(int id) {
    EntityGraph<IssueEntity> graph = em.createEntityGraph(IssueEntity.class);
    graph.addAttributeNodes("issueStatus", "tracker", "journals");

    Map<String, Object> hints = new HashMap<>();
    hints.put("javax.persistence.fetchgraph", graph);

    return em.find(IssueEntity.class, id, hints);
  }
}
