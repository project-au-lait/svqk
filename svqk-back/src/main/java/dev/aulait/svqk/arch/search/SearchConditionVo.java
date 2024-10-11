package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
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

  public int getOffset() {
    return (int) pageable.getOffset();
  }

  public int getPageSize() {
    return pageable.getPageSize();
  }

  public Sort getSort() {
    return pageable.getSort();
  }
}
