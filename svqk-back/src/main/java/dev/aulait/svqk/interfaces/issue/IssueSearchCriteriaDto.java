package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.search.PageControlDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class IssueSearchCriteriaDto { // <.>
  // <.>
  private String text;
  private List<IssueStatusDto> issueStatuses = new ArrayList<>();
  private LocalDate dueDate;
  private boolean subjectOnly;

  @Schema(required = true)
  private PageControlDto pageControl = new PageControlDto();
}
