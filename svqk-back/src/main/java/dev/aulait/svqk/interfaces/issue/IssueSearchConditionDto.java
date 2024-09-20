package dev.aulait.svqk.interfaces.issue;

import java.util.ArrayList;
import java.util.List;

import dev.aulait.svqk.arch.search.ConjunctionCd;
import dev.aulait.svqk.arch.search.FieldConditionVo;
import dev.aulait.svqk.arch.search.OperatorCd;
import dev.aulait.svqk.arch.search.SearchConditionDto;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SortOrderDto;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.domain.issue.IssueStatusEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class IssueSearchConditionDto extends SearchConditionDto {
  private String text;
  private List<IssueStatusDto> issueStatuses;
  private String dueDate;
  private boolean subjectOnly;

  public SearchConditionVo buildVo() {
    List<FieldConditionVo> fields = new ArrayList<>();

    fields.add(FieldConditionVo.of("subject", OperatorCd.LIKE, ConjunctionCd.OR, text));
    if (!subjectOnly) {
      fields.add(FieldConditionVo.of("description", OperatorCd.LIKE, ConjunctionCd.OR, text));
    }

    fields.add(
        FieldConditionVo.of("issueStatus", OperatorCd.IN, BeanUtils.mapAll(issueStatuses, IssueStatusEntity.class)));
    fields.add(FieldConditionVo.of("dueDate", dueDate));

    if(this.getSortOrders().isEmpty()) {
      SortOrderDto sortOrderDto = new SortOrderDto();
      sortOrderDto.setField("id");
      sortOrderDto.setAsc(false);
      this.getSortOrders().add(sortOrderDto);
    }

    return SearchConditionVo.of(fields, build());
  }
}