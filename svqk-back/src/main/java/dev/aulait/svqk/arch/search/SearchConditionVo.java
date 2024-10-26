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
public class SearchConditionVo {

  private Class<?> mainEntity;

  @Builder.Default private List<JoinVo> joins = new ArrayList<>();

  @Builder.Default private List<FieldConditionVo> fieldConditions = new ArrayList<>();

  private Pageable pageable;

  @Getter(lazy = true)
  private final int offset = (int) pageable.getOffset();

  @Getter(lazy = true)
  private final int pageSize = pageable.getPageSize();

  @Getter(lazy = true)
  private final Sort sort = pageable.getSort();
}
