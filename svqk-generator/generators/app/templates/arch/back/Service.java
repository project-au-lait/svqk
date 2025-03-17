package <%= domainPkgNm %>;

import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchResult;
import dev.aulait.svqk.arch.jpa.SearchUtils;
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
    return <%= entityNmCamel %>Repository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public <%= entityNmPascal %>Entity save(<%= entityNmPascal %>Entity entity) {
    return <%= entityNmCamel %>Repository.save(entity);
  }

  public SearchResult<<%= entityNmPascal %>Entity> search(SearchCriteria criteria) {
    return SearchUtils.search(em, criteria);
  }
}
