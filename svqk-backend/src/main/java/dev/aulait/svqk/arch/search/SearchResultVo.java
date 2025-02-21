package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SearchResultVo<T> {

  @Builder.Default private List<T> list = new ArrayList<>();

  private PageResultDto pageResult;
}
