package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.domain.issue.IssueStatusService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Path(IssueStatusController.ISSUE_STATUSES_PATH)
@RequiredArgsConstructor
public class IssueStatusController {

  private final IssueStatusService service;

  static final String ISSUE_STATUSES_PATH = "/issue-statuses";

  @GET
  public List<IssueStatusDto> get() { // <.>
    return BeanUtils.mapAll(service.findAll(), IssueStatusDto.class); // <.>
  }
}
