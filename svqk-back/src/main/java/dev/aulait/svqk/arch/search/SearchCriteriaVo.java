package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Value
@Builder
public class SearchCriteriaVo {

  private String select;

  @Builder.Default private List<FieldCriteriaVo> fieldCriteria = new ArrayList<>();

  private Pageable pageable;

  private int pageNumsRange;

  @Getter(lazy = true)
  private final int offset = (int) pageable.getOffset();

  @Getter(lazy = true)
  private final int pageSize = pageable.getPageSize();

  @Getter(lazy = true)
  private final int pageNumber = pageable.getPageNumber() + 1;

  @Getter(lazy = true)
  private final Sort sort = pageable.getSort();
}
