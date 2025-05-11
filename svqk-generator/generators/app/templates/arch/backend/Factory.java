package <%= interfacesPkgNm %>;

import dev.aulait.sqb.SearchCriteria;
import dev.aulait.sqb.SearchCriteriaBuilder;
import dev.aulait.sqb.SearchResult;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.util.BeanUtils.MappingConfig;
import <%= domainPkgNm %>.<%= entityNmPascal %>Entity;
import <%= interfacesPkgNm %>.<%= entityNmPascal %>Controller.<%= entityNmPascal %>SearchResultDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class <%= entityNmPascal %>Factory {

  private MappingConfig<<%= entityNmPascal %>Entity, <%= entityNmPascal %>Dto> searchResultConfig =
      BeanUtils.buildConfig(<%= entityNmPascal %>Entity.class, <%= entityNmPascal %>Dto.class)
          .build(); 

  public SearchCriteria build(<%= entityNmPascal %>SearchCriteriaDto criteria) {
    return new SearchCriteriaBuilder()
        .select("SELECT <%= entityNmFirstLetter %> FROM <%= entityNmPascal %>Entity <%= entityNmFirstLetter %>")
        .orderBy(criteria.getSortOrders())
        .build(criteria.getPageControl());
  }

  public <%= entityNmPascal %>SearchResultDto build(SearchResult<<%= entityNmPascal %>Entity> vo) { 
    return BeanUtils.map(searchResultConfig, vo, <%= entityNmPascal %>SearchResultDto.class); 
  }
}
