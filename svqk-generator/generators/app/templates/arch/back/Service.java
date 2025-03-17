package <%= domainPkgNm %>;

import dev.aulait.svqk.arch.jpa.SearchUtils;
import dev.aulait.svqk.arch.search.SearchCriteriaVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import dev.aulait.svqk.arch.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class <%= entityNmPascal %>Service {

  private final EntityManager em;

  private final <%= entityNmPascal %>Repository <%= entityNmCamel %>Repository;

  public <%= entityNmPascal %>Entity find(<%= idField.javaType %> id) {
    return <%= entityNmCamel %>Repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(1));
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

  public SearchResultVo<<%= entityNmPascal %>Entity> search(SearchCriteriaVo criteria) {
    return SearchUtils.search(em, criteria);
  }
}
