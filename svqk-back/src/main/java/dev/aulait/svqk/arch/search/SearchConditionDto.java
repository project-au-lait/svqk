package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SearchConditionDto {
  private int pageNumber;
  private int pageSize;
  private List<SortOrderDto> sortOrders = new ArrayList<>();
}
