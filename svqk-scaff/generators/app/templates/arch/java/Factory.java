package <%= interfacesPkgNm %>;

import static dev.aulait.svqk.arch.search.ComparisonOperatorCd.*;
import static dev.aulait.svqk.arch.search.LogicalOperatorCd.*;
import dev.aulait.svqk.arch.search.SearchCriteriaBuilder;
import dev.aulait.svqk.arch.search.SearchCriteriaVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.util.BeanUtils.MappingConfig;
import <%= domainPkgNm %>.<%= entityNmPascal %>Entity;
import <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmPascal %>SearchResultDto;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class <%= entityNmPascal %>Factory {

  private MappingConfig<<%= entityNmPascal %>Entity, <%= entityNmPascal %>Dto> searchResultConfig =
      BeanUtils.buildConfig(<%= entityNmPascal %>Entity.class, <%= entityNmPascal %>Dto.class)
          .build(); 

  public SearchCriteriaVo build(<%= entityNmPascal %>SearchCriteriaDto criteria) {
    return new SearchCriteriaBuilder()
        .select("SELECT i FROM <%= entityNmPascal %>Entity i")
        .build(criteria.getPageControl());
  }

  public <%= entityNmPascal %>SearchResultDto build(SearchResultVo<<%= entityNmPascal %>Entity> vo) { 
    return BeanUtils.map(searchResultConfig, vo, <%= entityNmPascal %>SearchResultDto.class); 
  }
}