package dev.aulait.svqk.arch.search;

import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.util.ReflectionUtils;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchResultFactory {

  public static <T, S extends SearchResultDto<T>> S build(
      SearchConditionVo condition, SearchResultVo<?> result, Class<T> dtoType, Class<S> type) {

    S dto = ReflectionUtils.newInstance(type);

    List<T> list = BeanUtils.mapAll(result.getList(), dtoType);

    dto.setList(list);
    dto.setCount(result.getCount());
    dto.setStart(result.getCount() < 1 ? 0 : condition.getOffset() + 1);
    dto.setLimit(condition.getPageSize());

    return dto;
  }
}
