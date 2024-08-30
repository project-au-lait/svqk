package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import lombok.Data;

@Data
public class SearchConditionDto {
  private int pageNumber;
  private int pageSize;
  private List<SortOrderDto> sortOrders = new ArrayList<>();

  public Pageable build() {

    List<Order> orders = sortOrders.stream()
        .map(sortOrder -> new Order(sortOrder.isAsc() ? Direction.ASC : Direction.DESC, sortOrder.getField())).toList();

    Sort sort = Sort.by(orders);

    int pageNumberSd = pageNumber - 1;
    return PageRequest.of(pageNumberSd < 0 ? 0 : pageNumberSd, pageSize < 1 ? 10 : pageSize, sort);
  }
}
