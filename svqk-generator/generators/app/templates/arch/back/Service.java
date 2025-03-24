package <%= domainPkgNm %>;

import static dev.aulait.svqk.arch.jpa.JpaUtils.findByIdAsResource;

import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchResult;
import dev.aulait.sqb.jpa.JpaSearchQueryExecutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class <%= entityNmPascal %>Service {

  private final EntityManager em;

  private final <%= entityNmPascal %>Repository <%= entityNmCamel %>Repository;

  private final JpaSearchQueryExecutor searchExecutor;

  public <%= entityNmPascal %>Entity find(<%= idField.javaType %> id) {
    return findByIdAsResource(<%= entityNmCamel %>Repository, id);
  }

  @Transactional
  public <%= entityNmPascal %>Entity save(<%= entityNmPascal %>Entity entity) {
    return <%= entityNmCamel %>Repository.save(entity);
  }

  @Transactional
  public void delete(<%= entityNmPascal %>Entity entity) {
    <%= entityNmPascal %>Entity managedEntity = em.merge(entity);
    <%= entityNmCamel %>Repository.delete(managedEntity);
  }

  public SearchResult<<%= entityNmPascal %>Entity> search(SearchCriteria criteria) {
    return searchExecutor.search(em, criteria);
  }

}
