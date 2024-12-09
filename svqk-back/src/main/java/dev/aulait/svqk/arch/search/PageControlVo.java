package dev.aulait.svqk.arch.search;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PageControlVo {

  private long count;
  private int pageSize;
  private int pageNumber;
  private int pageNumsRange;
  private int offset;
}
