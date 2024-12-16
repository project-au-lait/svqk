package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.search.SearchConditionDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // <.>
public class IssueSearchConditionDto extends SearchConditionDto { // <.>
  // <.>
  private String text;
  private List<IssueStatusDto> issueStatuses = new ArrayList<>();
  private LocalDate dueDate;
  private boolean subjectOnly;
}
