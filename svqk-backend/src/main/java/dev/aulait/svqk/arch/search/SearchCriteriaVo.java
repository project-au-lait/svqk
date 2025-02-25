package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SearchCriteriaVo {
  private String select;
  @Builder.Default private List<FieldCriteriaVo> fieldCriteria = new ArrayList<>();
  private PageControlDto pageControl;
}
