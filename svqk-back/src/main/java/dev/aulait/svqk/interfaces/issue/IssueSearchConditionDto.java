package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.search.SearchConditionDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class IssueSearchConditionDto extends SearchConditionDto {
  private String text;
  private List<IssueStatusDto> issueStatuses = new ArrayList<>();
  private String dueDate;
  private boolean subjectOnly;
}
