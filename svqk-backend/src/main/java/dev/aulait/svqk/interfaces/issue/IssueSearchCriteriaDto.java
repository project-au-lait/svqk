package dev.aulait.svqk.interfaces.issue;

import dev.aulait.sqb.PageControl;
import dev.aulait.sqb.SortOrder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class IssueSearchCriteriaDto { // <.>
  // <.>
  private String text;
  private List<String> issueStatuses = new ArrayList<>();
  private LocalDate dueDate;
  private boolean subjectOnly;

  @Schema(required = true)
  private PageControl pageControl = new PageControl();

  private List<SortOrder> sortOrders = new ArrayList<>();
}
