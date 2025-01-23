package <%= domainPkgNm %>;

import dev.aulait.svqk.arch.jpa.SearchUtils;
import dev.aulait.svqk.arch.search.SearchCriteriaVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class <%= entityNmPascal %>Service {

  private final EntityManager em;

  private final <%= entityNmPascal %>Repository <%= entityNmCamel %>Repository;

  public <%= entityNmPascal %>Entity find(int id) {

    return <%= entityNmCamel %>Repository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  @Transactional
  public <%= entityNmPascal %>Entity save(<%= entityNmPascal %>Entity entity) {

    return <%= entityNmCamel %>Repository.save(entity);
  }

  public SearchResultVo<<%= entityNmPascal %>Entity> search(SearchCriteriaVo criteria) {
    return SearchUtils.search(em, criteria);
  }
}
