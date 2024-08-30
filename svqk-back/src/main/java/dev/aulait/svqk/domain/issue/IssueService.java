package dev.aulait.svqk.domain.issue;

import static dev.aulait.svqk.arch.jpa.JpaRepositoryHandler.findByIdAsResource;

import dev.aulait.svqk.arch.jpa.SearchUtils;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class IssueService {

  private final IssueRepository repository;
  private final EntityManager em;

  @Transactional
  public IssueEntity save(IssueEntity entity) {
    return repository.save(entity);
  }

  public IssueEntity find(int id) {
    return findByIdAsResource(repository, id);
  }

  public SearchResultVo<IssueEntity> search(SearchConditionVo condition) {
    return SearchUtils.search(em, IssueEntity.class, condition);
  }

}
