package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.search.SearchCriteriaDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // <.>
public class IssueSearchCriteriaDto extends SearchCriteriaDto { // <.>
  // <.>
  private String text;
  private List<IssueStatusDto> issueStatuses = new ArrayList<>();
  private LocalDate dueDate;
  private boolean subjectOnly;
}
