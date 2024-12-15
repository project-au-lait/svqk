package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SearchResultDto;
import dev.aulait.svqk.arch.search.SearchResultVo;
import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.arch.web.ApiPath;
import dev.aulait.svqk.domain.issue.IssueEntity;
import dev.aulait.svqk.domain.issue.IssueService;
import dev.aulait.svqk.domain.issue.JournalEntity;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.RequiredArgsConstructor;

@Path(IssueController.ISSUES_PATH)
@RequiredArgsConstructor
public class IssueController {

  private final IssueService service;

  private final IssueFactory factory;

  static final String ISSUES_PATH = ApiPath.ROOT + "/issues";

  static final String ISSUES_GET_PATH = "{issueId}";

  static final String ISSUES_TRACKING_GET_PATH = "tracking";

  static final String ISSUES_SEARCH_PATH = "search";

  public static class IssueSearchResultDto extends SearchResultDto<IssueDto> {} // <.>

  @POST
  public int create(@Valid IssueDto dto) { // <.>

    // <.>
    IssueEntity entity = BeanUtils.map(dto, IssueEntity.class);
    IssueEntity savedEntity = service.save(entity);

    return savedEntity.getId();
  }

  @PUT
  public int update(@Valid IssueUpdateDto dto) {
    IssueEntity issue = BeanUtils.map(dto.getIssue(), IssueEntity.class);
    JournalEntity journal = BeanUtils.map(dto.getJournal(), JournalEntity.class);

    IssueEntity updatedIssue = service.update(issue, journal);

    return updatedIssue.getId();
  }

  @GET
  @Path(ISSUES_GET_PATH)
  public IssueDto get(@PathParam("issueId") int id) {
    IssueEntity entity = service.find(id);

    return BeanUtils.map(entity, IssueDto.class);
  }

  @POST
  @Path(ISSUES_SEARCH_PATH)
  public IssueSearchResultDto search(IssueSearchConditionDto dto) { // <.>
    // <.>
    SearchConditionVo vo = factory.build(dto);
    SearchResultVo<IssueEntity> result = service.search(vo);

    return factory.build(result);
  }

  @GET
  @Path(ISSUES_TRACKING_GET_PATH)
  public IssueTrackingDto getTracking() {
    return factory.buildIssueTracking(service.getTracking());
  }
}
