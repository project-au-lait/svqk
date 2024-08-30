package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class SearchConditionVo {

  @Builder.Default
  private List<FieldConditionVo> fieldConditions = new ArrayList<>();

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

  public static SearchConditionVo of(List<FieldConditionVo> fieldConditions, Pageable pageable) {
    return SearchConditionVo.builder().fieldConditions(fieldConditions).pageable(pageable).build();
  }

}
